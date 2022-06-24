package com.company.sangyo.core.kanban;

import com.company.sangyo.core.NativeQueryService;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service(QualifiedRateService.NAME)
public class QualifiedRateServiceBean implements QualifiedRateService {

    protected static final Logger log = LoggerFactory.getLogger(QualifiedRateServiceBean.class);

    @Inject
    private NativeQueryService nativeQueryService;

    @Override
    public Collection<KeyValueEntity> getQualifiedRateCollection(String startDate, String endDate, String tradeDress) {
        List<KeyValueEntity> keyValueEntities = new ArrayList();

        String sql = "select\n" +
                "\tdate_trunc('day', yf.warehousing_time) as mydate,\n" +
                "\tcount(distinct yf.screen_id_id) as mycount,\n" +
                "\tcount(distinct yrr.screen_id) as mycount1\n" +
                "from\n" +
                "\tyalong_finished yf\n" +
                "left join yalong_work_order ywo on\n" +
                "\tywo.id = yf.work_order_id\n" +
                "left join yalong_order yo on\n" +
                "\tyo.id = ywo.order_id_id\n" +
                "left join yalong_in_plant_part_number yippn on\n" +
                "\tyippn.id = yf.in_plant_part_number_id\n" +
                "left join yalong_rework_record yrr on\n" +
                "\tyrr.screen_id = yf.screen_id_id and yrr.source_procedure_id in (\n" +
                "\tselect\n" +
                "\t\typfdap.procedure_id\n" +
                "\tfrom\n" +
                "\t\tyalong_process_flow_details_and_procedure ypfdap\n" +
                "\twhere\n" +
                "\t\typfdap.process_flow_id = ywo.process_flow_id_id AND yrr.rework_type <> 'B')" +
                "where\n" +
                "\tyo.order_type = '销售订单'\n" +
                tradeDress +
                "\tand yf.warehousing_time between (" + startDate + ") and (" + endDate + ")\n" +
                "group by\n" +
                "\tmydate\n" +
                "order by\n" +
                "\tmydate";
        List<Object[]> listData = nativeQueryService.getListData(sql);

        for (int i = 0; i < listData.size(); i++) {
            KeyValueEntity keyValueEntity = new KeyValueEntity();
            keyValueEntity.setValue("stationNum", listData.get(i)[0].toString().substring(5, 10));
            Long warehouseInQuantity = (Long) listData.get(i)[1];
            Long reworkQuantity = (Long) listData.get(i)[2];
            double qualifiedRate;
            if (warehouseInQuantity == 0L) {
                qualifiedRate = 0;
            } else {
                qualifiedRate = new BigDecimal((float) (warehouseInQuantity - reworkQuantity) / warehouseInQuantity).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            log.info("日期{},入仓{},返工{},合格率{}", listData.get(i)[0].toString().substring(5, 10), warehouseInQuantity.toString(), reworkQuantity, qualifiedRate);
            keyValueEntity.setValue("minutest", String.valueOf(qualifiedRate));
            keyValueEntity.setValue("total", String.valueOf(warehouseInQuantity));
            keyValueEntity.setValue("Number", String.valueOf(warehouseInQuantity - reworkQuantity));
            keyValueEntities.add(keyValueEntity);
        }
        return keyValueEntities;
    }


    @Override
    public List getAllTradeDress(String tradeDress) {
        String sql = "select commodity_form from yalong_in_plant_part_number where " +
                "delete_ts is null and commodity_form LIKE'%" + tradeDress + "%' group by commodity_form ";
        List list = nativeQueryService.getListData(sql);
        log.info("获取所有商品形态，{}", list);
        return list;
    }

    @Override
    public List getAllTradeDress() {
        String sql = "select commodity_form from yalong_in_plant_part_number where " +
                "delete_ts is null and commodity_form is not null group by commodity_form ";
        List list = nativeQueryService.getListData(sql);
        log.info("获取所有商品形态，{}", list);
        return list;
    }

    @Override
    public List getAllProcedureName() {
        String sql = "select name from yalong_procedure where delete_ts is null and name is not null group by name";
        List list = nativeQueryService.getListData(sql);
        log.info("获取所有工序，{}", list);
        return list;
    }
}
