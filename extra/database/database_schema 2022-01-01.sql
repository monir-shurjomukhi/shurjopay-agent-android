-- Dumping structure for table sp2_admin_db.qr_settlement_ac_info
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

-- Dumping data for table sp2_admin_db.qr_settlement_ac_info: ~0 rows (approximately)
/*!40000 ALTER TABLE `qr_settlement_ac_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `qr_settlement_ac_info` ENABLE KEYS */;

-- Dumping structure for table sp2_admin_db.qr_shop_info
CREATE TABLE IF NOT EXISTS `qr_shop_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `shop_or_business_name` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `tin_no` varchar(100) CHARACTER SET ascii COLLATE ascii_general_ci DEFAULT NULL,
  `trade_licence` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci DEFAULT NULL COMMENT 'file',
  `shop_addess` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL COMMENT 'Holding number, Street, etc',
  `shop_address_division_id` bigint NOT NULL COMMENT 'fk-comes from division table -id',
  `shop_address_district_id` bigint NOT NULL COMMENT 'fk-comes from district table -id',
  `shop_address_police_station_id` bigint NOT NULL COMMENT 'fk comes from police station table -id',
  `business_type` bigint NOT NULL COMMENT 'fk-comes from business_type table -id',
  `shop_size` varchar(50) NOT NULL COMMENT 'predefined dropdown',
  `shop_gps_location` varchar(255) NOT NULL,
  `shop_front_img` varchar(255) NOT NULL,
  `shop_owner_id` bigint NOT NULL COMMENT 'fk-shop owner info table',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL COMMENT 'comes from users table -id',
  `updated_by` bigint DEFAULT NULL COMMENT 'comes from users table -id',
  `deleted_by` bigint DEFAULT NULL COMMENT 'comes from users table -id',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '1=active , 0=inactive , 2=deleted',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=ascii;

-- Dumping data for table sp2_admin_db.qr_shop_info: ~0 rows (approximately)
/*!40000 ALTER TABLE `qr_shop_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `qr_shop_info` ENABLE KEYS */;

-- Dumping structure for table sp2_admin_db.qr_shop_owner_info
CREATE TABLE IF NOT EXISTS `qr_shop_owner_info` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `owner_name` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `nid_no` varchar(20) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `contact_no` varchar(16) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL COMMENT 'mobile_no',
  `email_address` varchar(100) CHARACTER SET ascii COLLATE ascii_general_ci DEFAULT NULL,
  `nid_front` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `nid_back` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `tin_no` varchar(100) CHARACTER SET ascii COLLATE ascii_general_ci DEFAULT NULL,
  `ower_img` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `perm_addess` varchar(255) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `perm_division_id` bigint NOT NULL COMMENT 'fk-comes from division table -id',
  `perm_district_id` bigint NOT NULL COMMENT 'fk-comes from district table -id',
  `perm_police_station_id` bigint NOT NULL COMMENT 'fk comes from police station table -id',
  `owner_dob` varchar(50) CHARACTER SET ascii COLLATE ascii_general_ci NOT NULL,
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '1=active , 0=inactive , 2=deleted',
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `created_by` bigint DEFAULT NULL COMMENT 'comes from users table -id',
  `updated_by` bigint DEFAULT NULL COMMENT 'comes from users table -id',
  `deleted_by` bigint DEFAULT NULL COMMENT 'comes from users table -id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=ascii;
