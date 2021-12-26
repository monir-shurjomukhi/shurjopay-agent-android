

DROP TABLE "shop_owner_info";
CREATE TABLE "shop_owner_info" (
        "id"                        BIGINT,
        "owner_name"                TEXT NOT NULL,
        "nid_no"                    TEXT NOT NULL,
        "contact_no"                TEXT NOT NULL,
        "email_address"             TEXT,
        "nid_front"                 TEXT NOT NULL,
        "nid_back"                  TEXT NOT NULL,
        "ower_img"                  TEXT NOT NULL,
        "perm_addess"               TEXT NOT NULL,
        "perm_division_id"          BIGINT NOT NULL,
        "perm_district_id"          BIGINT NOT NULL,
        "perm_police_station_id"    BIGINT NOT NULL,
        "owner_dob"                 DATETIME NOT NULL,
        "is_sync"                   BOOLEAN DEFAULT 0,
        CONSTRAINT "pk_shop_owner_info_id" PRIMARY KEY("id")
);
DROP TABLE "shop_info";
CREATE TABLE "shop_info" (
        "id"                        BIGINT,
        "shop_owner_id"             BIGINT,
        "shop_or_business_name"     TEXT NOT NULL,
        "tin_no"                    TEXT,
        "trade_licence"             TEXT NOT NULL,
        "shop_addess"               TEXT NOT NULL,
        "shop_address_division_id"  BIGINT NOT NULL,
        "shop_address_district_id"  BIGINT NOT NULL,
        "shop_address_police_station_id"    BIGINT NOT NULL,
        "business_type"             BIGINT NOT NULL,
        "shop_size"                 TEXT NOT NULL,
        "shop_gps_location"         TEXT NOT NULL,
        "shop_front_img"            TEXT NOT NULL,
        "is_sync"                   BOOLEAN DEFAULT 0,
        CONSTRAINT fk_shop_info_shop_owner_id FOREIGN KEY (shop_owner_id) REFERENCES shop_owner_info(id),
        CONSTRAINT "pk_shop_info_id" PRIMARY KEY("id")
);
DROP TABLE "settlement_ac_info";
CREATE TABLE "settlement_ac_info" (
        "id"                        BIGINT,
        "shop_owner_id"             BIGINT,
        "is_sync"                   BOOLEAN DEFAULT 0,
        CONSTRAINT fk_settlement_ac_info_shop_owner_id FOREIGN KEY (shop_owner_id) REFERENCES shop_owner_info(id),
        CONSTRAINT "pk_shop_info_id" PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS `qr_settlement_ac_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_category` varchar(50) NOT NULL COMMENT 'dropdown - Existing Bank/MFS/New',
  `account_name` varchar(255) NOT NULL,
  `account_no` varchar(255) NOT NULL,
  `bank_name_or_mfs` bigint NOT NULL COMMENT 'dropdown - comes from bank_name table -id',
  `bank_branch_name` bigint NOT NULL COMMENT 'dropdown- comes from branch_name table',
  `routing_no` varchar(255) DEFAULT NULL,
  `account_type` varchar(255) NOT NULL COMMENT 'dropdown - Current/ Savings/ Personal/ Agent/ Merchant',
  `is_mfs` varchar(255) DEFAULT NULL,
  `shop_owner_id` bigint NOT NULL COMMENT 'fk- shop owner info',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `deleted_by` bigint DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '1=active , 0=inactive , 2= deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=ascii;
