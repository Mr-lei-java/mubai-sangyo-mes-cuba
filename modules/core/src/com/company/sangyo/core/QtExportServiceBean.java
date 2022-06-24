package com.company.sangyo.core;

import com.alibaba.excel.EasyExcel;
import com.company.sangyo.core.production.QualityTraceabilityService;
import com.company.sangyo.entity.basic.*;
import com.company.sangyo.entity.production.ProductionRecords;
import com.company.sangyo.entity.production.WorkOrderAndScreen;
import com.company.sangyo.entity.storage.Finished;
import com.company.sangyo.entity.storage.QualityTraceability;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service(QtExportService.NAME)
public class QtExportServiceBean implements QtExportService {
    private static final Logger log = LoggerFactory.getLogger(QtExportServiceBean.class);

    @Inject
    private DataManager dataManager;

    @Inject
    private QualityTraceabilityService qualityTraceabilityService;
    @Inject
    private Persistence persistence;

    @Override
    public byte[] export(Set<Finished> fINISHEDsTableSelected) {

        //导出模板规则
        LoadContext<QtExportRules> qtExportRulesLoadContext = LoadContext.create(QtExportRules.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QtExportRules e")
        ).setView("qtExportRules-view");
        QtExportRules qtExportRules = dataManager.load(qtExportRulesLoadContext);
        LoadContext<QtExportRuleItem> qtExportRuleItemLoadContext = LoadContext.create(QtExportRuleItem.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QtExportRuleItem e where e.qtExportRules =:rule order by e.createTs ")
                        .setParameter("rule", qtExportRules))
                .setView("qtExportRuleItem-view");
        List<QtExportRuleItem> qtExportRuleItems = dataManager.loadList(qtExportRuleItemLoadContext);
        if (qtExportRuleItems.size() > 0) {
            if (qtExportRuleItems.get(0).getSerialNumber() != null) {
                Collections.sort(qtExportRuleItems, new Comparator<QtExportRuleItem>() {
                    @Override
                    public int compare(QtExportRuleItem o1, QtExportRuleItem o2) {
                        int a = Integer.parseInt(o1.getSerialNumber());
                        int b = Integer.parseInt(o2.getSerialNumber());
                        int i = a - b;
                        if (i == 0) {
                            return a - b;
                        }
                        return i;
                    }
                });
            }
        }
        LoadContext<Procedure> procedureLoadContext = LoadContext.create(Procedure.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Procedure e "))
                .setView("procedure-view");
        List<Procedure> procedures = dataManager.loadList(procedureLoadContext);
        //将选择要导出的网版的质量追溯记录组合一下
        HashMap<Object, HashMap<Object, String>> map1 = new HashMap<Object, HashMap<Object, String>>();
        fINISHEDsTableSelected.stream().forEach(finished -> {
            //上传参数记录
            List<ProductionRecords> productionRecordsList = qualityTraceabilityService.getProductionRecords(finished.getScreenId());
            HashMap<Object, String> map2 = new HashMap<>();
            for (ProductionRecords productionRecords : productionRecordsList) {
                //料号参数+实测值
                map2.put(productionRecords.getParameter(), productionRecords.getParameterValue());
            }
            map1.put(finished.getId(), map2);
        });

        //定义表头
        List<List<String>> headList = new ArrayList<List<String>>();

        //定义内容
        List<List<Object>> dataList = new ArrayList<List<Object>>();

        //要导出的所有参数
        List<QtExportRuleItemAndPartNumberParameters> processParameters = new ArrayList<>();

        for (QtExportRuleItem qtExportRuleItem : qtExportRuleItems) {
            Procedure procedure = qtExportRuleItem.getProcedure();
            List<QtExportRuleItemAndPartNumberParameters> qtExportRuleItemAndPartNumberParameters =
                    qtExportRuleItem.getQtExportRuleItemAndPartNumberParameters();
            if (qtExportRuleItemAndPartNumberParameters.size() > 0) {
                Collections.sort(qtExportRuleItemAndPartNumberParameters, new Comparator<QtExportRuleItemAndPartNumberParameters>() {
                    @Override
                    public int compare(QtExportRuleItemAndPartNumberParameters o1, QtExportRuleItemAndPartNumberParameters o2) {
                        int a = o1.getSerialNumber();
                        int b = o2.getSerialNumber();
                        int i = a - b;
                        if (i == 0) {
                            return a - b;
                        }
                        return i;
                    }
                });
            }
            processParameters.addAll(qtExportRuleItemAndPartNumberParameters);
        }

        //表头添加一些基础属性
        List<String> head0 = new ArrayList<String>();
        head0.add("网版");
        headList.add(head0);
        List<String> head1 = new ArrayList<String>();
        head1.add("客户名称");
        headList.add(head1);
        List<String> head2 = new ArrayList<String>();
        head2.add("厂内料号");
        headList.add(head2);
        List<String> head3 = new ArrayList<String>();
        head3.add("客户料号");
        headList.add(head3);
        List<String> head4 = new ArrayList<String>();
        head4.add("备货料号");
        headList.add(head4);
        List<String> head5 = new ArrayList<String>();
        head5.add("商品形态");
        headList.add(head5);
        List<String> head6 = new ArrayList<String>();
        head6.add("厂内订单号");
        headList.add(head6);
        List<String> head7 = new ArrayList<String>();
        head7.add("订单项目");
        headList.add(head7);
        //表头添加设置的参数
        for (QtExportRuleItemAndPartNumberParameters partNumberParameters : processParameters) {
            List<String> head = new ArrayList<String>();
            head.add(partNumberParameters.getPartNumberParameters().getName());
            headList.add(head);
        }
        for (Procedure procedure : procedures) {
            if (qtExportRules.getIsExportTurnoverTime()) {
                List<String> head = new ArrayList<String>();
                head.add(procedure.getName() + "进站时间");
                headList.add(head);
                List<String> head8 = new ArrayList<String>();
                head8.add(procedure.getName() + "出站时间");
                headList.add(head8);
                if (qtExportRules.getIsExportOperatingPersonnel()) {
                    List<String> headName = new ArrayList<String>();
                    headName.add(procedure.getName() + "操作人");
                    headList.add(headName);
                } else {
                    List<String> headName = new ArrayList<String>();
                    headName.add(procedure.getName() + "操作人");
                    headList.add(headName);
                }
            }
        }

        //表内容
        for (Finished finished : fINISHEDsTableSelected) {
            List<Object> data = new ArrayList<Object>();
            //基础属性
            data.add(finished.getScreenId().getScreenCode());
            if (finished.getScreenId().getWorkOrder() != null) {
                Screen screen = getScreen(finished.getScreenId().getId());
                setTableContent(data, screen);
                String stockNumber = getStockNumber(finished.getScreenId().getId());
                setTableContents(data, screen, stockNumber);
            } else {
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
            }

            //配置参数
            for (QtExportRuleItemAndPartNumberParameters partNumberParameters : processParameters) {
                data.add(map1.get(finished.getId()).get(partNumberParameters.getPartNumberParameters()));
            }
            LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                    LoadContext.createQuery(" select e from sangyo_QualityTraceability e " +
                            "where e.screen.id =:id and e.deleteTs is null order by e.createTs")
                            .setParameter("id", finished.getScreenId().getId())
            ).setView("qualityTraceability-view");
            List<QualityTraceability> qualityTraceabilities = dataManager.loadList(qualityTraceabilityLoadContext);
            boolean a = false;
            Map map = new HashMap();
            log.info(finished.getScreenId().getScreenCode());
            getOperatingTime(qtExportRules, procedures, dataList, data, qualityTraceabilities, map);
        }

        //文件名
        String fileName = "成品仓数据.xlsx";

        //这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName).head(headList).sheet("质量追溯").doWrite(dataList);

        return getBytesByFile(fileName);
    }

    @Override
    public byte[] exportQualityTraceability(Set<QualityTraceability> qualityTraceabilitySet) {

        //导出模板规则
        LoadContext<QtExportRules> qtExportRulesLoadContext = LoadContext.create(QtExportRules.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QtExportRules e")
        ).setView("qtExportRules-view");
        QtExportRules qtExportRules = dataManager.load(qtExportRulesLoadContext);
        LoadContext<QtExportRuleItem> qtExportRuleItemLoadContext = LoadContext.create(QtExportRuleItem.class).setQuery(
                LoadContext.createQuery("select e from sangyo_QtExportRuleItem e where e.qtExportRules =:rule")
                        .setParameter("rule", qtExportRules))
                .setView("qtExportRuleItem-view");
        List<QtExportRuleItem> qtExportRuleItems = dataManager.loadList(qtExportRuleItemLoadContext);
        if (qtExportRuleItems.size() > 0) {
            if (qtExportRuleItems.get(0).getSerialNumber() != null) {
                Collections.sort(qtExportRuleItems, new Comparator<QtExportRuleItem>() {
                    @Override
                    public int compare(QtExportRuleItem o1, QtExportRuleItem o2) {
                        int a = Integer.parseInt(o1.getSerialNumber());
                        int b = Integer.parseInt(o2.getSerialNumber());
                        int i = a - b;
                        if (i == 0) {
                            return a - b;
                        }
                        return i;
                    }
                });
            }
        }
        LoadContext<Procedure> procedureLoadContext = LoadContext.create(Procedure.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Procedure e "))
                .setView("procedure-view");
        List<Procedure> procedures = dataManager.loadList(procedureLoadContext);

        //将选择要导出的网版的质量追溯记录组合一下
        HashMap<Object, HashMap<Object, String>> map1 = new HashMap<Object, HashMap<Object, String>>();
        qualityTraceabilitySet.stream().forEach(qualityTraceability -> {
            //上传参数记录
            List<ProductionRecords> productionRecordsList = qualityTraceabilityService.getProductionRecords(qualityTraceability.getScreen());
            //工序记录
            HashMap<Object, String> map2 = new HashMap<>();
            for (ProductionRecords productionRecords : productionRecordsList) {
                //料号参数+实测值
                map2.put(productionRecords.getParameter(), productionRecords.getParameterValue());
            }
            map1.put(qualityTraceability.getId(), map2);
        });

        //定义表头
        List<List<String>> headList = new ArrayList<List<String>>();

        //定义内容
        List<List<Object>> dataList = new ArrayList<List<Object>>();

        //要导出的所有参数
        List<QtExportRuleItemAndPartNumberParameters> processParameters = new ArrayList<>();

        for (QtExportRuleItem qtExportRuleItem : qtExportRuleItems) {
            List<QtExportRuleItemAndPartNumberParameters> qtExportRuleItemAndPartNumberParameters =
                    qtExportRuleItem.getQtExportRuleItemAndPartNumberParameters();
            if (qtExportRuleItemAndPartNumberParameters.size() > 0) {
                Collections.sort(qtExportRuleItemAndPartNumberParameters, new Comparator<QtExportRuleItemAndPartNumberParameters>() {
                    @Override
                    public int compare(QtExportRuleItemAndPartNumberParameters o1, QtExportRuleItemAndPartNumberParameters o2) {
                        int a = o1.getSerialNumber();
                        int b = o2.getSerialNumber();
                        int i = a - b;
                        if (i == 0) {
                            return a - b;
                        }
                        return i;
                    }
                });
            }
            processParameters.addAll(qtExportRuleItemAndPartNumberParameters);
        }
        //表头添加一些基础属性
        List<String> head0 = new ArrayList<String>();
        head0.add("网版");
        headList.add(head0);
        List<String> head1 = new ArrayList<String>();
        head1.add("客户名称");
        headList.add(head1);
        List<String> head2 = new ArrayList<String>();
        head2.add("厂内料号");
        headList.add(head2);
        List<String> head3 = new ArrayList<String>();
        head3.add("客户料号");
        headList.add(head3);
        List<String> head4 = new ArrayList<String>();
        head4.add("备货料号");
        headList.add(head4);
        List<String> head5 = new ArrayList<String>();
        head5.add("商品形态");
        headList.add(head5);
        List<String> head6 = new ArrayList<String>();
        head6.add("厂内订单号");
        headList.add(head6);
        List<String> head7 = new ArrayList<String>();
        head7.add("订单项目");
        headList.add(head7);
        //表头添加设置的参数
        for (QtExportRuleItemAndPartNumberParameters partNumberParameters : processParameters) {
            List<String> head = new ArrayList<String>();
            head.add(partNumberParameters.getPartNumberParameters().getName());
            headList.add(head);
        }
        for (Procedure procedure : procedures) {
            if (qtExportRules.getIsExportTurnoverTime()) {
                List<String> head = new ArrayList<String>();
                head.add(procedure.getName() + "进站时间");
                headList.add(head);
                List<String> head8 = new ArrayList<String>();
                head8.add(procedure.getName() + "出站时间");
                headList.add(head8);
                if (qtExportRules.getIsExportOperatingPersonnel()) {
                    List<String> headName = new ArrayList<String>();
                    headName.add(procedure.getName() + "操作人");
                    headList.add(headName);
                } else {
                    List<String> headName = new ArrayList<String>();
                    headName.add(procedure.getName() + "操作人");
                    headList.add(headName);
                }
            }
        }

        //表内容
        for (QualityTraceability qualityTraceability : qualityTraceabilitySet) {
            List<Object> data = new ArrayList<Object>();
            //基础属性
            data.add(qualityTraceability.getScreen().getScreenCode());
            if (qualityTraceability.getScreen().getWorkOrder() != null) {
                Screen screen = getScreen(qualityTraceability.getScreen().getId());
                setTableContent(data, screen);
                String stockNumber = getStockNumber(qualityTraceability.getScreen().getId());
                setTableContents(data, screen, stockNumber);
            } else {
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("");
            }
            //配置参数
            for (QtExportRuleItemAndPartNumberParameters partNumberParameters : processParameters) {
                data.add(map1.get(qualityTraceability.getId()).get(partNumberParameters.getPartNumberParameters()));
            }
            LoadContext<QualityTraceability> qualityTraceabilityLoadContext = LoadContext.create(QualityTraceability.class).setQuery(
                    LoadContext.createQuery(" select e from sangyo_QualityTraceability e " +
                            "where e.screen.id =:id and e.deleteTs is null order by e.createTs")
                            .setParameter("id", qualityTraceability.getScreen().getId())
            ).setView("qualityTraceability-view");
            List<QualityTraceability> qualityTraceabilities = dataManager.loadList(qualityTraceabilityLoadContext);
            boolean a = false;
            Map map = new HashMap();
            log.info(qualityTraceability.getScreen().getScreenCode());
            getOperatingTime(qtExportRules, procedures, dataList, data, qualityTraceabilities, map);
        }

        //文件名
        String fileName = "质量追溯数据.xlsx";

        //这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName).head(headList).sheet("质量追溯").doWrite(dataList);

        return getBytesByFile(fileName);
    }

    private void setTableContents(List<Object> data, Screen screen, String stockNumber) {
        data.add(stockNumber);
        data.add(screen.getWorkOrder().getCustomerPartNumber() != null ? screen.getWorkOrder().getCustomerPartNumber().getCommodityForm() : "");
        data.add(screen.getWorkOrder().getOrderId().getInPlantOrder() != null ? screen.getWorkOrder().getOrderId().getInPlantOrder() : "");
        String OrderProject = screen.getWorkOrder().getOrderId().getOrderProject() != null ? String.valueOf(screen.getWorkOrder().getOrderId().getOrderProject()) : "";
        getOrderProject(data, OrderProject);
    }

    private void setTableContent(List<Object> data, Screen screen) {
        data.add(screen.getWorkOrder().getOrderId().getCustomer() != null ? screen.getWorkOrder().getOrderId().getCustomer().getName() : "");
        data.add(screen.getWorkOrder().getInPlantPartNumberId() != null ? screen.getWorkOrder().getInPlantPartNumberId().getInPlantPartNumberName() : "");
        data.add(screen.getWorkOrder().getCustomerPartNumber() != null ? screen.getWorkOrder().getCustomerPartNumber().getName() : "");
    }

    private void getOperatingTime(QtExportRules qtExportRules, List<Procedure> procedures, List<List<Object>> dataList, List<Object> data, List<QualityTraceability> qualityTraceabilities, Map map) {
        boolean a;
        for (Procedure procedure : procedures) {
            a = false;
            if (qtExportRules.getIsExportTurnoverTime() != null) {
                if (qtExportRules.getIsExportTurnoverTime()) {
                    for (QualityTraceability qualityTrceability : qualityTraceabilities) {
                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        if (procedure.getName().equals(qualityTrceability.getDispatchList().getProcedure().getName())) {
                            if (qualityTrceability.getGetInTime() != null) {
                                String getInTime = df.format(qualityTrceability.getGetInTime());
                                map.put(procedure.getName() + "进站时间", getInTime + "-" + qualityTrceability.getDispatchList().getName());
                            } else {
                                map.put(procedure.getName() + "进站时间", "");
                            }
                            if (qualityTrceability.getOutboundTime() != null) {
                                String outboundTime = df.format(qualityTrceability.getOutboundTime());
                                map.put(procedure.getName() + "出站时间", outboundTime + "-" + qualityTrceability.getDispatchList().getName());
                            } else {
                                map.put(procedure.getName() + "出站时间", "");
                            }
                            if (qtExportRules.getIsExportOperatingPersonnel()) {
                                if (qualityTrceability.getOperatingPersonnel() != null) {
                                    map.put(procedure.getName() + "操作人", qualityTrceability.getOperatingPersonnel().getName());
                                } else {
                                    map.put(procedure.getName() + "操作人", "");
                                }
                            }
                            a = true;
                        }
                    }
                }
            }
            if (!a) {
                map.put(procedure.getName() + "进站时间", "");
                map.put(procedure.getName() + "出站时间", "");
                map.put(procedure.getName() + "操作人", "");
            }
        }
        for (Procedure procedure : procedures) {
            data.add(map.get(procedure.getName() + "进站时间"));
            data.add(map.get(procedure.getName() + "出站时间"));
            data.add(map.get(procedure.getName() + "操作人"));
        }
        dataList.add(data);
    }

    private void getOrderProject(List<Object> data, String orderProject) {
        switch (orderProject) {
            case "CUSTOMER_ORDER":
                orderProject = "客户订单";
                break;
            case "CUSTOMER_TEST":
                orderProject = "不良品换货";
                break;
            case "REPLACEMENT_DEFECTIVEPRO_DUCTS":
                orderProject = "客户订单";
                break;
            case "GIVE_AWAY":
                orderProject = "赠送";
                break;
            case "IN_PLANT_EXPERIMENT":
                orderProject = "厂内实验";
                break;
        }
        data.add(orderProject);
    }

    private String getStockNumber(UUID screen) {
        Transaction transaction = persistence.createTransaction();
        String InPlantPartNumberName = "";
        try {
            LoadContext<WorkOrderAndScreen> workOrderAndScreenLoadContext = LoadContext.create(WorkOrderAndScreen.class).setQuery(
                    LoadContext.createQuery("select e from sangyo_WorkOrderAndScreen e " +
                            "where  e.screen.id =:screen order by e.createTs DESC")
                            .setParameter("screen", screen)
            ).setView("workOrderAndScreen-view_2");
            List<WorkOrderAndScreen> workOrderAndScreens = dataManager.loadList(workOrderAndScreenLoadContext);
            if (workOrderAndScreens.size() > 0) {
                if (workOrderAndScreens.get(0).getPreWorkOrder() != null) {
                    if (workOrderAndScreens.get(0).getPreWorkOrder().getInPlantPartNumberId() != null) {
                        InPlantPartNumberName = workOrderAndScreens.get(0).getPreWorkOrder().getInPlantPartNumberId().getInPlantPartNumberName();
                    }
                }
            }
        } catch (Throwable e) {
            log.error("Error", e);
            return InPlantPartNumberName;
        } finally {
            transaction.end();
        }
        return InPlantPartNumberName;
    }
    private Screen getScreen(UUID screen){
        LoadContext<Screen> warehouseLoadContext = LoadContext.create(Screen.class).setQuery(
                LoadContext.createQuery("select e from sangyo_Screen  e where  e.id=:screen")
                        .setParameter("screen", screen)
        ).setView("screen-view_3");
        return dataManager.load(warehouseLoadContext);
    }


    public static byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<String>();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<String>();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> list = new ArrayList<List<Object>>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<Object>();
            data.add("字符串" + i);
            data.add(new Date());
            data.add(0.56);
            list.add(data);
        }
        return list;
    }
}
