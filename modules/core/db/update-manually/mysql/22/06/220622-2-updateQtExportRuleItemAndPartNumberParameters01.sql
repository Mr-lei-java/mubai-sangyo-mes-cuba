alter table SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS add constraint FK_SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS_ON_QT_EXPORT_RULE_ITEM foreign key (QT_EXPORT_RULE_ITEM_ID) references SANGYO_QT_EXPORT_RULE_ITEM(ID);
create index IDX_SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS_ON_QT_EXPORT_RULE_ITEM on SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS (QT_EXPORT_RULE_ITEM_ID);