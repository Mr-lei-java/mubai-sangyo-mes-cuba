alter table SANGYO_ORDER_ALLOCATION_RULE_ITEM add constraint FK_SANGYO_ORDER_ALLOCATION_RULE_ITEM_ON_WORKORDERTANDARD_VALUE foreign key (WORKORDERTANDARD_VALUE_ID) references SANGYO_PART_NUMBER_PARAMETERS(ID);
create index IDX_SANGYO_ORDER_ALLOCATION_RULE_ITEM_ON_WORKORDERTANDARD_VALUE on SANGYO_ORDER_ALLOCATION_RULE_ITEM (WORKORDERTANDARD_VALUE_ID);
