package com.company.sangyo.core;

import com.company.sangyo.CachedRepositories;
import com.company.sangyo.entity.production.WorkOrder;
import com.company.sangyo.repositories.WorkOrderRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Service(CRUDDemoService.NAME)
public class CRUDDemoServiceBean implements CRUDDemoService {

    @Inject
    private CachedRepositories cachedRepositories;

    @Override
    public void create() {
        WorkOrderRepository workOrderRepository = cachedRepositories.get(WorkOrderRepository.class);
        WorkOrder workOrder = new WorkOrder();
        workOrder.setName("99999");
        workOrderRepository.commit(workOrder);
    }

    @Override
    public void retrieve() {
        WorkOrderRepository workOrderRepository = cachedRepositories.get(WorkOrderRepository.class);
        WorkOrder workOrder = workOrderRepository.load(UUID.fromString("26be7ce9-ec71-f444-3175-3cad62b1ded5"), "_local");
        //查列表
        List<WorkOrder> workOrders = workOrderRepository.loadList("_local");
    }

    @Override
    public void update() {
        WorkOrderRepository workOrderRepository = cachedRepositories.get(WorkOrderRepository.class);
        WorkOrder workOrder = workOrderRepository.load(UUID.fromString("26be7ce9-ec71-f444-3175-3cad62b1ded5"), "_local");
        workOrder.setName("");
        workOrderRepository.commit(workOrder);
    }

    @Override
    public void delete() {

    }
}
