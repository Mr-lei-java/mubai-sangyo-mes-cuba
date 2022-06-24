-- begin SANGYO_PART_NUMBER_PARAMETERS
create table SANGYO_PART_NUMBER_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    UPLOAD_FILES_ID varchar(32),
    USE_ varchar(50),
    PARAMETERSTYPE varchar(50),
    GROUP_VALUE_VERIFICATION_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PART_NUMBER_PARAMETERS
-- begin SANGYO_QT_EXPORT_RULES
create table SANGYO_QT_EXPORT_RULES (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    IS_EXPORT_TURNOVER_TIME boolean,
    IS_EXPORT_OPERATING_PERSONNEL boolean,
    --
    primary key (ID)
)^
-- end SANGYO_QT_EXPORT_RULES
-- begin SANGYO_REWORK_RECORD
create table SANGYO_REWORK_RECORD (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    REWORK_PROCEDURE_ID_ID varchar(32),
    OPERATING_PERSONNEL_ID varchar(32),
    OPERATE_TIME datetime(3),
    REMARK varchar(255),
    SOURCE_WAREHOUSE_ID varchar(32),
    REWORK_TYPE varchar(50),
    SCREEN_ID varchar(32),
    SOURCE_PROCEDURE_ID varchar(32),
    IS_REPEATED boolean,
    IS_ACTIVE boolean,
    --
    primary key (ID)
)^
-- end SANGYO_REWORK_RECORD
-- begin SANGYO_RAW
create table SANGYO_RAW (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    WAREHOUSE_ID_ID varchar(32),
    MATERIAL varchar(255),
    NUMBER_ varchar(255),
    REMARK varchar(255),
    REMARK2 varchar(255),
    REMARK3 varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_RAW
-- begin SANGYO_ERP_SYNCHRONOUS_RECORD
create table SANGYO_ERP_SYNCHRONOUS_RECORD (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    TYPE_ varchar(255),
    DATE_TIME datetime(3),
    PARAMETER_ longtext,
    ORIGIN longtext,
    IS_SYNCHRONOUS boolean,
    IS_PROCESSED boolean,
    OPERATOR longtext,
    OPERATE_DATE datetime(3),
    REMARK longtext,
    PARAMETER_PASSING longtext,
    --
    primary key (ID)
)^
-- end SANGYO_ERP_SYNCHRONOUS_RECORD
-- begin SANGYO_ENTRY_AND_EXIT_VERIFICATION_RULE_ITEM
create table SANGYO_ENTRY_AND_EXIT_VERIFICATION_RULE_ITEM (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PARAMETERS_ID_ID varchar(32) not null,
    ALIGNMENT_METHOD varchar(50) not null,
    SCREEN_PARAMETERS_ID varchar(32) not null,
    INBOUND_AND_OUTBOUND_CALIBRATION_RULES_ID varchar(32) not null,
    --
    primary key (ID)
)^
-- end SANGYO_ENTRY_AND_EXIT_VERIFICATION_RULE_ITEM
-- begin SANGYO_WAREHOUSE
create table SANGYO_WAREHOUSE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    TYPE_ varchar(255),
    QUANTITY_SCREEN_VERSION integer,
    PROCEDURE_ID varchar(32),
    LOW_WATER_LEVEL integer,
    HIGH_WATER_LEVEL integer,
    IS_SYNCHRONIZE_ERP boolean,
    --
    primary key (ID)
)^
-- end SANGYO_WAREHOUSE
-- begin SANGYO_SCREEN
create table SANGYO_SCREEN (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SCREEN_CODE varchar(255) not null,
    WORK_ORDER_ID varchar(32),
    SCREEN_STATUS varchar(50),
    CURRENT_PROCEDURE_ID varchar(32),
    SCREEN_CURRENT_POSITION varchar(255),
    PART_NUMBER_ID varchar(32),
    OUT_PW_TIME datetime(3),
    REMARK varchar(255),
    FRAME varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_SCREEN
-- begin SANGYO_CUSTOMER_ANDCUSTOMER_DESCRIPTIVE_PARAMETERS
create table SANGYO_CUSTOMER_ANDCUSTOMER_DESCRIPTIVE_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CUSTOMER_ID_ID varchar(32) not null,
    CUSTOMER_DESCRIPTIVE_PARAMETERS_ID varchar(32) not null,
    TYPE_ varchar(255),
    PARAMETERS_VALUE varchar(255),
    UPLOAD_FILES_ID varchar(32),
    REMARK varchar(255),
    STANDARD_DEVIATION boolean,
    --
    primary key (ID)
)^
-- end SANGYO_CUSTOMER_ANDCUSTOMER_DESCRIPTIVE_PARAMETERS
-- begin SANGYO_ORDER
create table SANGYO_ORDER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ORDER_CODE varchar(255),
    ORDER_QUANTITY integer,
    ORDER_PROPERTIES varchar(50),
    ORDER_TIME datetime(3),
    DELIVERY_TIME datetime(3),
    ORDER_TYPE varchar(50) not null,
    IN_PLANT_ORDER varchar(255) not null,
    SOURCE varchar(255),
    ORDER_DATA_UPDATE varchar(50),
    CUSTOMER_ID varchar(32),
    ORDER_STATUS varchar(50),
    ORDER_PRIORITY varchar(255),
    ORDER_PROJECT varchar(50),
    COLUMN_REMARK varchar(255),
    REMARK2 varchar(255),
    REMARK3 varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_ORDER
-- begin SANGYO_SEMI
create table SANGYO_SEMI (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    WAREHOUSE_ID_ID varchar(32),
    SCREEN_ID_ID varchar(32) not null,
    WAREHOUSING_TIME datetime(3),
    OUT_WAREHOUSE_TIME datetime(3),
    STATUS varchar(255),
    REWORK_TYPE varchar(50),
    OPERATING_PERSONNEL_ID varchar(32),
    REMARK varchar(255),
    REMARK4 varchar(255),
    PROCESSING_MODE varchar(50),
    PROCESSING_TIME datetime(3),
    WORK_ORDER_ID varchar(32),
    REMARK2 varchar(255),
    REMARK3 varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_SEMI
-- begin SANGYO_OPERATION_INSTRUCTION
create table SANGYO_OPERATION_INSTRUCTION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SERIAL_NUMBER varchar(255),
    PROCEDURE_ID varchar(32) not null,
    ISSUE_PARAMETERS_ID varchar(32),
    IS_CALCULATED_RSD boolean,
    CUSTOMER_DESCRIPTIVE_PARAMETER_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_OPERATION_INSTRUCTION
-- begin SANGYO_DISPATCH_LIST_AND_SCREEN
create table SANGYO_DISPATCH_LIST_AND_SCREEN (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    DISPATCH_LIST_ID varchar(32),
    SCREEN_ID varchar(32),
    STATUS boolean,
    --
    primary key (ID)
)^
-- end SANGYO_DISPATCH_LIST_AND_SCREEN
-- begin SANGYO_WORKSHOP
create table SANGYO_WORKSHOP (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    --
    primary key (ID)
)^
-- end SANGYO_WORKSHOP
-- begin SANGYO_WORK_ORDER_AND_SCREEN
create table SANGYO_WORK_ORDER_AND_SCREEN (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    WORK_ORDER_ID varchar(32),
    SCREEN_ID varchar(32),
    IS_DISTRIBUTED boolean,
    PRE_WORK_ORDER_ID varchar(32),
    NEXT_WORK_ORDER_ID varchar(32),
    WORK_ORDER_SCREEN_STATUS varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_WORK_ORDER_AND_SCREEN
-- begin SANGYO_STATION
create table SANGYO_STATION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    STATION_NUMBER varchar(255),
    WORKSHOP_ID_ID varchar(32),
    STATION_STATUS varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_STATION
-- begin SANGYO_UPLOAD_PARAMETERS
create table SANGYO_UPLOAD_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    LABEL_TEMPLATE_ID varchar(32),
    PARAMETERS_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_UPLOAD_PARAMETERS
-- begin SANGYO_STANDING_TIME_CHECK
create table SANGYO_STANDING_TIME_CHECK (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    IN_PLANT_PART_NUMBER_ID varchar(32),
    PROCEDURE_ID varchar(32),
    STANDING_TIME time(3),
    --
    primary key (ID)
)^
-- end SANGYO_STANDING_TIME_CHECK
-- begin SANGYO_SOP_MANAGEMENT
create table SANGYO_SOP_MANAGEMENT (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    FILE_DESCRIPTOR_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_SOP_MANAGEMENT
-- begin SANGYO_INVENTORY_STATION_ENTITY
create table SANGYO_INVENTORY_STATION_ENTITY (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    INVENTORY_TIME datetime(3),
    SCREEN_CODE_ID varchar(32),
    PROCEDURE_NAME_ID varchar(32),
    PHYSICAL_NUMBER varchar(255),
    COMPARE_RESULTS boolean,
    EXCEPTION_DESCRIPTION longtext,
    WHETHER_OPERATE boolean,
    OPERATE_RESULT varchar(255),
    REMARK varchar(255),
    ANNAL_USER varchar(255),
    IS_CONFIRMED boolean,
    INVENTORY_STATION_RECORDING_ENTITY_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_INVENTORY_STATION_ENTITY
-- begin SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS
create table SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    QT_EXPORT_RULE_ITEM_ID varchar(32),
    PART_NUMBER_PARAMETERS_ID varchar(32),
    SERIAL_NUMBER integer,
    --
    primary key (ID)
)^
-- end SANGYO_QT_EXPORT_RULE_ITEM_AND_PART_NUMBER_PARAMETERS
-- begin SANGYO_LABEL_TEMPLATE
create table SANGYO_LABEL_TEMPLATE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    EMPLATE_NAME varchar(255) not null,
    IS_PRINT_WORK_ORDER boolean,
    TEMPLATE_CODING varchar(255) not null,
    STATUS varchar(255),
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_LABEL_TEMPLATE
-- begin SANGYO_ISSUE_KANBAN
create table SANGYO_ISSUE_KANBAN (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ISSUE_TIME datetime(3),
    ISSUE_USER_ID varchar(32),
    ISSUE_TOTAL integer,
    ACCOMPLISH_QUANTITY integer,
    PERFECTION_TIME datetime(3),
    PERFECTION_RATE varchar(255),
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_ISSUE_KANBAN
-- begin SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS
create table SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SERIAL_NUMBER integer,
    IN_PLANT_PART_NUMBER_ID varchar(32),
    PART_NUMBER_PARAMETER_ID_ID varchar(32),
    TYPE_ varchar(50),
    UPLOAD_FILES_ID varchar(32),
    PARAMETER_VALUE varchar(255),
    GROUP_ varchar(255),
    TOLERANCE varchar(255),
    RANGE_ varchar(255),
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_IN_PLANT_PART_NUMBER_AND_PART_NUMBER_PARAMETERS
-- begin SANGYO_STATION_AND_USER
create table SANGYO_STATION_AND_USER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    USER_ID varchar(32),
    STATION_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_STATION_AND_USER
-- begin SANGYO_STOCK_ORDER
create table SANGYO_STOCK_ORDER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    IN_PLANT_PART_NUMBER_ID varchar(32),
    QUANTITY_STOCK integer,
    ORDER_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_STOCK_ORDER
-- begin SANGYO_PROCESS_PARAMETERS
create table SANGYO_PROCESS_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SERIAL_NUMBER varchar(255),
    PROCEDURE_ID varchar(32),
    REMARK varchar(255),
    PARAMETER_NAME_ID varchar(32),
    IS_VERIFIED boolean,
    STANDARD_VALUE_ID varchar(32),
    TYPE_ varchar(255),
    IS_REQUIRED boolean,
    CHECK_WAY varchar(50),
    SOURCE varchar(50) not null,
    IS_CALCULATED_RSD boolean,
    CUSTOMER_DESCRIPTIVE_PARAMETER_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PROCESS_PARAMETERS
-- begin SANGYO_CUSTOMER
create table SANGYO_CUSTOMER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CUSTOMER_CODING varchar(255) not null,
    NAME varchar(255),
    SOURCE varchar(255),
    DELIVERY_ADDRESS varchar(255),
    CONTACTS varchar(255),
    CONTACT_NUMBER varchar(255),
    LABEL_TEMPLATE varchar(255),
    PERFECT_STATE varchar(255),
    SHIPPING_REPORT_PRINT_TEMPLATE_ID varchar(32),
    CUSTOMER_SERVICE_TECHNICIAN varchar(255),
    CLIENTS_CATEGORIES varchar(255) not null,
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_CUSTOMER
-- begin SANGYO_PROCESS_FLOW_ID
create table SANGYO_PROCESS_FLOW_ID (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SERIAL_NUMBER integer,
    PROCESS_FLOW_ID_ID varchar(32),
    PROCEDURE_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PROCESS_FLOW_ID
-- begin SANGYO_IN_PLANT_PART_NUMBER
create table SANGYO_IN_PLANT_PART_NUMBER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    INVENTORY_CODING varchar(255) not null,
    QUALITY_MAN_FLAG boolean,
    PERFECT_STATE varchar(255),
    NAME varchar(255),
    PART_NUMBER_LABEL_TEMPLATE varchar(255),
    FILM_CODE varchar(255) not null,
    INVENTORY_CLASSIFICATION varchar(255),
    LOT_NUMBER_MANAGEMENT boolean,
    QUALITY_PERIOD_UNIT varchar(255),
    QUALITY_DAYNUM varchar(255),
    SCREEN_FRAME_SIZE_MODEL varchar(255),
    NET_YARN_SPECIFICATION varchar(255),
    COMMODITY_FORM_ID varchar(255),
    WIRE_MESH_ANGLE varchar(255),
    REMARKS varchar(255),
    TENSION varchar(255),
    SOURCE varchar(255),
    STATUS varchar(255),
    ITEM_NATURE varchar(255),
    MESH_WIDTH varchar(255),
    COMMODITY_FORM varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_IN_PLANT_PART_NUMBER
-- begin SANGYO_ITEM_NUMBER_TEMPLATE
create table SANGYO_ITEM_NUMBER_TEMPLATE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ITEM_NUMBER_PARAMETER_TEMPLATE_ID varchar(32),
    PART_NUMBER_PARAMETERS_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_ITEM_NUMBER_TEMPLATE
-- begin SANGYO_CUSTOMER_PART_NUMBER
create table SANGYO_CUSTOMER_PART_NUMBER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    INVENTORY_CODING varchar(255) not null,
    PERFECT_STATE varchar(255),
    NAME varchar(255),
    CUSTOMER_ID varchar(32),
    SCREEN_FRAME_SIZE_MODEL varchar(255),
    CUSTOMER_PART_NUMBER_STATUS varchar(255),
    ANGLE varchar(255),
    LOT_NUMBER_MANAGEMENT boolean,
    QUALITY_PERIOD_UNIT varchar(255),
    QUALITY_DAYNUM varchar(255),
    INVENTORY_CLASSIFICATION varchar(255),
    P_T_VALUE_LINE_NUMBER varchar(255),
    TEN_ERR double precision,
    MEMB_TH_SPC double precision,
    MEMB_TH_ERR double precision,
    TTL_TH_SPC double precision,
    QUALITY_MAN_FLAG boolean,
    SOURCE varchar(255),
    TTL_TH_ERR double precision,
    LINE_WID_SPC double precision,
    LINE_WIT_ERR double precision,
    PT_VAL double precision,
    PT_ERR double precision,
    X_DIS_SPC double precision,
    X_DIS_ERR double precision,
    Y_DIS_SPC double precision,
    Y_DIS_ERR double precision,
    NOTE varchar(255),
    DW_ATTCH1_ID varchar(32),
    DW_ATTCH2_ID varchar(32),
    NET_YARN_SPECIFICATION varchar(255),
    TENSION varchar(255),
    STEEL_MESH_FORMAT_SIZE varchar(255),
    ITEM_DESCRIPTION varchar(255),
    MESH_SPECIFICATION varchar(255),
    CUSTOMER_PART_NUMBER_NATURE varchar(255),
    COMMODITY_FORM varchar(255),
    COMMODITY_FORM_ID varchar(255),
    MESH_WIDTH varchar(255),
    COPYRIGHT_VERSION_NUMBER varchar(255),
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_CUSTOMER_PART_NUMBER
-- begin SANGYO_CUSTOMER_PART_NUMBER_ANDIN_PLANT_PART_NUMBER
create table SANGYO_CUSTOMER_PART_NUMBER_ANDIN_PLANT_PART_NUMBER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NUMERICAL_ORDER integer,
    CUSTOMER_PART_NUMBER_ID varchar(32) not null,
    IN_PLANT_PART_NUMBER_ID varchar(32) not null,
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_CUSTOMER_PART_NUMBER_ANDIN_PLANT_PART_NUMBER
-- begin SANGYO_ORDER_AND_CUSTOMER_PART_NUMBER
create table SANGYO_ORDER_AND_CUSTOMER_PART_NUMBER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ORDER_ID varchar(32),
    DCONSIGNDATE varchar(255),
    IN_PLANT_PARTDCONSIGNDATE varchar(255),
    CUSTOMER_PART_NUMBER_ID_ID varchar(32),
    ORDER_QUANTITY integer not null,
    ORDER_STATUS integer,
    --
    primary key (ID)
)^
-- end SANGYO_ORDER_AND_CUSTOMER_PART_NUMBER
-- begin SANGYO_WORK_ORDER
create table SANGYO_WORK_ORDER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ORDER_ID_ID varchar(32),
    CUSTOMER_PART_NUMBER_ID varchar(32),
    IN_PLANT_PART_NUMBER_ID_ID varchar(32),
    OUTPUT_WAREHOUSE_ID varchar(32) not null,
    PROCESS_FLOW_ID_ID varchar(32) not null,
    ORDER_PRIORITY varchar(255),
    IN_PLANT_PART_CONSIGN_DATE varchar(255),
    ORDER_PRODUCTION_QUANTITY integer not null,
    DEFAULT_ORDER_PRODUCTION_QUANTITY integer,
    PLAN_PRODUCTION_QUANTITY integer not null,
    COMPLETED_QUANTITY integer,
    SUPPLEMENTARY_QUANTITY integer,
    QUANTITY_ISSUED integer,
    SHIP_QUANTITY integer,
    CHANGED_QUANTITY integer,
    SCRAP_RECORD integer,
    ELIGIBILITY integer,
    REMARK varchar(255),
    NAME varchar(255),
    ORDER_DATA_UPDATE varchar(50),
    DISTRIBUTE datetime(3),
    DELIVERY_TIME datetime(3),
    IS_COPY boolean,
    HAS_CHANGED boolean,
    IS_SUPPLEMENT boolean,
    STATUS varchar(50),
    SHIP_STATUS varchar(50),
    DISTRIBUTED_STATUS varchar(50),
    --
    primary key (ID)
)^
-- end SANGYO_WORK_ORDER
-- begin SANGYO_DISPATCH_LIST
create table SANGYO_DISPATCH_LIST (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    PROCEDURE_ID varchar(32),
    ISSUE_TIME datetime(3),
    WORK_ORDER_ID varchar(32),
    PLANNED_QUANTITY integer,
    RECEIVE_COUNT integer,
    ACCOMPLISH_QUANTITY integer,
    STATUS varchar(255),
    TYPE_ varchar(50),
    WHETHER_CHECK boolean,
    ISSUE_QUANTITY integer,
    DISPATCH_LIST_ACCOMPLISH_QUANTITY integer,
    WHETHER_MARK_RED boolean,
    DUE_TIME date,
    --
    primary key (ID)
)^
-- end SANGYO_DISPATCH_LIST
-- begin SANGYO_QUALITY_TRACEABILITY
create table SANGYO_QUALITY_TRACEABILITY (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    GET_IN_TIME datetime(3),
    WORK_ORDER_ID varchar(32),
    SCREEN_ID varchar(32) not null,
    DISPATCH_LIST_ID varchar(32),
    OUTBOUND_TIME datetime(3),
    OUT_WAREHOUSE_TIME datetime(3),
    OPERATING_PERSONNEL_ID varchar(32),
    OUTBOUND_OPERATING_PERSONNEL_ID varchar(32),
    STATE varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_QUALITY_TRACEABILITY
-- begin SANGYO_ISSUE_KANBAN_AND_DISPATCH_LIST
create table SANGYO_ISSUE_KANBAN_AND_DISPATCH_LIST (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    ISSUE_KANBAN_ID varchar(32),
    ISSUE_QUANTITY integer,
    DUE_TIME datetime(3),
    INITIAL_QUANTITY integer,
    PROCEDURE_ACCOMPLISH_QUANTITY integer,
    PLANNED_QUANTITY integer,
    ISSUE_TIME datetime(3),
    ISSUE_USER_ID varchar(32),
    CUSTOMER_ID varchar(32),
    DELIVERY_TIME datetime(3),
    DISPATCH_LIST_ID varchar(32),
    IN_PLANT_PART_NUMBER_ID varchar(32),
    PROCEDURE_ID varchar(32),
    DISPATCH_LIST_ACCOMPLISH_QUANTITY integer,
    PERFECTION_RATE varchar(255),
    WORK_ORDER_NUMBER varchar(255),
    FACTORY_ORDER_NUMBER varchar(255),
    WHETHER_MARK_RED boolean,
    WHETHER_END boolean,
    --
    primary key (ID)
)^
-- end SANGYO_ISSUE_KANBAN_AND_DISPATCH_LIST
-- begin SANGYO_PRODUCTION_RECORDS
create table SANGYO_PRODUCTION_RECORDS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SCREEN_ID varchar(32),
    PARAMETER_ID varchar(32),
    PARAMETER_VALUE varchar(255),
    QUALITY_TRACEABILITY_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PRODUCTION_RECORDS
-- begin SANGYO_PROCEDURE
create table SANGYO_PROCEDURE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    PROCEDURE_CODE varchar(255),
    PROCEDURE_STATUS varchar(50),
    STANDING_TIME time(3),
    IS_ALLOWED_PDA boolean,
    REMARK varchar(255),
    WORK_ORDER_ID varchar(32),
    PROCESS_FLOW_ID varchar(32),
    LABEL_TEMPLATE_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PROCEDURE
-- begin SANGYO_INBOUND_AND_OUTBOUND_CALIBRATION_RULES
create table SANGYO_INBOUND_AND_OUTBOUND_CALIBRATION_RULES (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_INBOUND_AND_OUTBOUND_CALIBRATION_RULES
-- begin SANGYO_PROCESS_FLOW_DETAILS_AND_PROCEDURE
create table SANGYO_PROCESS_FLOW_DETAILS_AND_PROCEDURE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    IS_FIRST_PROCEDURE boolean,
    IS_LAST_PROCEDURE boolean,
    REMARK varchar(255),
    SERIAL_NUMBER integer,
    PROCESS_FLOW_ID varchar(32),
    PROCEDURE_ID varchar(32),
    PRE_PROCEDURE_ID_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PROCESS_FLOW_DETAILS_AND_PROCEDURE
-- begin SANGYO_ORDER_ALLOCATION_RULES
create table SANGYO_ORDER_ALLOCATION_RULES (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    RULE_CODING varchar(255),
    NAME varchar(255) not null,
    STATE varchar(50) not null,
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_ORDER_ALLOCATION_RULES
-- begin SANGYO_ORDER_ALLOCATION_RULE_ITEM
create table SANGYO_ORDER_ALLOCATION_RULE_ITEM (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SERIAL_NUMBER integer,
    SCREEN_SOURCE_ENUM varchar(50) not null,
    VERIFICATION_RULES varchar(255),
    SCREEN_PARAMETERS_ID varchar(32),
    SOURCE varchar(50) not null,
    CALIBRATION_METHOD varchar(50),
    WORKORDERTANDARD_VALUE_ID varchar(32),
    MUST_MATCH boolean,
    ORDER_ALLOCATION_RULES_ID varchar(32),
    IS_CALCULATED_RSD boolean,
    CUSTOMER_DESCRIPTIVE_PARAMETER_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_ORDER_ALLOCATION_RULE_ITEM
-- begin SANGYO_FINISHED
create table SANGYO_FINISHED (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SCREEN_ID_ID varchar(32) not null,
    REWORK_TYPE varchar(50),
    WAREHOUSE_ID_ID varchar(32),
    IN_PLANT_PART_NUMBER_ID varchar(32),
    OUT_WAREHOUSE_TIME datetime(3),
    OPERATING_PERSONNEL_ID varchar(32),
    WAREHOUSING_TIME datetime(3),
    WORK_ORDER_ID varchar(32),
    PROCESSING_MODE varchar(50),
    PROCESSING_TIME datetime(3),
    REL_WORK_ORDER_ID varchar(32),
    STATUS varchar(50),
    REMARK varchar(255),
    REMARK1 varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_FINISHED
-- begin SANGYO_ITEM_NUMBER_PARAMETER_TEMPLATE
create table SANGYO_ITEM_NUMBER_PARAMETER_TEMPLATE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_ITEM_NUMBER_PARAMETER_TEMPLATE
-- begin SANGYO_GROUP_VALUE_VERIFICATION
create table SANGYO_GROUP_VALUE_VERIFICATION (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SERIAL_NUMBER integer,
    PARAMETERS_NAME_ID varchar(32),
    CHECK_TYPE varchar(50),
    SOURCE varchar(50) not null,
    STANDARD_VALUE_ID varchar(32) not null,
    TIMING_CALIBRATION varchar(50) not null,
    IS_CALCULATED_RSD boolean,
    CUSTOMER_DESCRIPTIVE_PARAMETER_ID varchar(32),
    PROCEDURE_ID varchar(32),
    ORDER_ALLOCATION_RULES_ID varchar(32) not null,
    MUST_MATCH boolean,
    --
    primary key (ID)
)^
-- end SANGYO_GROUP_VALUE_VERIFICATION
-- begin SANGYO_INVENTORY_STATION_RECORDING_ENTITY
create table SANGYO_INVENTORY_STATION_RECORDING_ENTITY (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROCEDURE_NAME_ID varchar(32),
    INVENTORY_USER varchar(255),
    START_TIME datetime(3),
    END_TIME datetime(3),
    TOTAL varchar(255),
    COIL_DEFICIT varchar(255),
    SURPLUS_COIL varchar(255),
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_INVENTORY_STATION_RECORDING_ENTITY
-- begin SANGYO_MATERIAL_PARAMETERS
create table SANGYO_MATERIAL_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    LABEL_TEMPLATE_ID varchar(32),
    PARAMETERS_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_MATERIAL_PARAMETERS
-- begin SANGYO_INVENTORY_RECORDING
create table SANGYO_INVENTORY_RECORDING (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    WAREHOUSE_NAME varchar(255),
    INVENTORY_USER_ID varchar(32),
    START_TIME datetime(3),
    END_TIME datetime(3),
    TOTAL integer,
    WAREHOUSE_QUANTITY integer,
    NORMAL_NUMBER integer,
    COIL_DEFICIT integer,
    SURPLUS_COIL integer,
    REMARKS varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_INVENTORY_RECORDING
-- begin SANGYO_CUSTOMER_DESCRIPTIVE_PARAMETERS
create table SANGYO_CUSTOMER_DESCRIPTIVE_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    PARAMETERSTYPE varchar(255),
    IS_RSD boolean,
    --
    primary key (ID)
)^
-- end SANGYO_CUSTOMER_DESCRIPTIVE_PARAMETERS
-- begin SANGYO_STATION_AND_PROCEDURE
create table SANGYO_STATION_AND_PROCEDURE (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    PROCEDURE_ID varchar(32),
    STATION_ID_ID varchar(32),
    SERIAL_NUMBER integer,
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_STATION_AND_PROCEDURE
-- begin SANGYO_SCRAP_RECORD
create table SANGYO_SCRAP_RECORD (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SCREEN_ID varchar(32) not null,
    OPERATING_PERSONNEL_ID varchar(32),
    WAREHOUSING_TIME datetime(3),
    SOURCE_SCRAPPED varchar(255),
    REMARK varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_SCRAP_RECORD
-- begin SANGYO_UNQUALIFIED
create table SANGYO_UNQUALIFIED (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    SCREEN_ID_ID varchar(32) not null,
    WAREHOUSE_ID_ID varchar(32),
    OPERATING_PERSONNEL_ID varchar(32),
    OPERATE_TIME datetime(3),
    WORK_ORDER_ID varchar(32),
    REMARK2 varchar(255),
    REMARK3 varchar(255),
    INCOMING_CLASSIFICATION varchar(50),
    ABNORMAL_PARAMETER longtext,
    IS_PROCESSED boolean,
    REMARK varchar(255),
    PROCESSOR_ID varchar(32),
    PROCESSING_MODE varchar(50),
    SOURCE varchar(255),
    SALE_ORDER varchar(255),
    OPERATOR_NAME varchar(255),
    PROCESSING_TIME datetime(3),
    PROCEDURE_ID varchar(32),
    REWORK_TYPE varchar(50),
    IS_REPEATED boolean,
    --
    primary key (ID)
)^
-- end SANGYO_UNQUALIFIED
-- begin SANGYO_QT_EXPORT_RULE_ITEM
create table SANGYO_QT_EXPORT_RULE_ITEM (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    QT_EXPORT_RULES_ID varchar(32),
    PROCEDURE_ID varchar(32),
    SERIAL_NUMBER varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_QT_EXPORT_RULE_ITEM
-- begin SANGYO_PART_NUMBER_ORDER
create table SANGYO_PART_NUMBER_ORDER (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CUSTOMER_PART_NUMBER_ID varchar(32),
    ORDER_PRODUCTION_QUANTITY integer,
    IN_PLANT_PART_NUMBER_ID varchar(32),
    IN_PLANTORDER_PRODUCTION_QUANTITY integer,
    PLAN_PRODUCTION_QUANTITY integer,
    ORDER_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PART_NUMBER_ORDER
-- begin SANGYO_FACTORY_PRODUCTION_STANDARDS_ENTITY
create table SANGYO_FACTORY_PRODUCTION_STANDARDS_ENTITY (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    STANDARD_DEVIATION varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_FACTORY_PRODUCTION_STANDARDS_ENTITY
-- begin SANGYO_CUSTOMER_PART_NUMBER_AND_PART_NUMBER_PARAMETERS
create table SANGYO_CUSTOMER_PART_NUMBER_AND_PART_NUMBER_PARAMETERS (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    CUSTOMER_PART_NUMBER_ID varchar(32),
    PART_NUMBER_PARAMETER_ID_ID varchar(32),
    TYPE_ varchar(50),
    UPLOAD_FILES_ID varchar(32),
    SERIAL_NUMBER varchar(255),
    PARAMETER_VALUE varchar(255),
    REMARK varchar(255),
    RANGE_ varchar(255),
    TOLERANCE varchar(255),
    GROUP_ varchar(255),
    --
    primary key (ID)
)^
-- end SANGYO_CUSTOMER_PART_NUMBER_AND_PART_NUMBER_PARAMETERS
-- begin SANGYO_INVENTORY_CHECK
create table SANGYO_INVENTORY_CHECK (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    INVENTORY_TIME datetime(3),
    SCREEN_CODE_ID varchar(32),
    WAREHOUSE_NAME varchar(255),
    PHYSICAL_NUMBER varchar(255),
    COMPARE_RESULTS boolean,
    EXCEPTION_DESCRIPTION varchar(255),
    ANNAL_USER_ID varchar(32),
    WHETHER_OPERATE varchar(255),
    OPERATE_RESULT varchar(255),
    IS_CONFIRMED varchar(50),
    REMARK varchar(255),
    INVENTORY_RECORDING_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_INVENTORY_CHECK
-- begin SANGYO_PROCESS_FLOW
create table SANGYO_PROCESS_FLOW (
    ID varchar(32),
    VERSION integer not null,
    CREATE_TS datetime(3),
    CREATED_BY varchar(50),
    UPDATE_TS datetime(3),
    UPDATED_BY varchar(50),
    DELETE_TS datetime(3),
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    ORDER_ALLOCATION_RULES_ID varchar(32),
    IS_ALL_MATCHED boolean,
    PROCESS_NUMBER varchar(255),
    STATUS varchar(50),
    REMARKS varchar(255),
    WHETHERTART_WITH_FIRST_PROCESS_PROCESS boolean,
    WHETHER_ALL_MATCH boolean,
    WHETHER_END_AT_END_PROCESS boolean,
    OUTPUT_SCREEN_OUTPUT_TRANSFER_LIBRARY_CATEGORY_ID varchar(32),
    TYPE_LIBRARY varchar(50),
    FINISHED_ORDER_ALLOCATION_RULES_ID varchar(32),
    --
    primary key (ID)
)^
-- end SANGYO_PROCESS_FLOW
-- begin SANGYO_PROCESS_FLOW_WAREHOUSE_LINK
create table SANGYO_PROCESS_FLOW_WAREHOUSE_LINK (
    PROCESS_FLOW_ID varchar(32),
    WAREHOUSE_ID varchar(32),
    primary key (PROCESS_FLOW_ID, WAREHOUSE_ID)
)^
-- end SANGYO_PROCESS_FLOW_WAREHOUSE_LINK
-- begin SANGYO_GROUP_VALUE_VERIFICATION_PART_NUMBER_PARAMETERS_LINK
create table SANGYO_GROUP_VALUE_VERIFICATION_PART_NUMBER_PARAMETERS_LINK (
    GROUP_VALUE_VERIFICATION_ID varchar(32),
    PART_NUMBER_PARAMETERS_ID varchar(32),
    primary key (GROUP_VALUE_VERIFICATION_ID, PART_NUMBER_PARAMETERS_ID)
)^
-- end SANGYO_GROUP_VALUE_VERIFICATION_PART_NUMBER_PARAMETERS_LINK
-- begin SANGYO_PROCESS_FLOW_PROCEDURE_LINK
create table SANGYO_PROCESS_FLOW_PROCEDURE_LINK (
    PROCESS_FLOW_ID varchar(32),
    PROCEDURE_ID varchar(32),
    primary key (PROCESS_FLOW_ID, PROCEDURE_ID)
)^
-- end SANGYO_PROCESS_FLOW_PROCEDURE_LINK
-- begin SANGYO_QT_EXPORT_RULE_ITEM_PART_NUMBER_PARAMETERS_LINK
create table SANGYO_QT_EXPORT_RULE_ITEM_PART_NUMBER_PARAMETERS_LINK (
    QT_EXPORT_RULE_ITEM_ID varchar(32),
    PART_NUMBER_PARAMETERS_ID varchar(32),
    primary key (QT_EXPORT_RULE_ITEM_ID, PART_NUMBER_PARAMETERS_ID)
)^
-- end SANGYO_QT_EXPORT_RULE_ITEM_PART_NUMBER_PARAMETERS_LINK
