DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NULL DEFAULT '' comment 'string',
  `age` int unsigned NULL DEFAULT 0 comment 'integer',
  `school` varchar(64) NULL DEFAULT '' comment 'string',
  `true_false` tinyint(1) unsigned NULL DEFAULT 1 comment 'boolean',
  `time` bigint unsigned NULL DEFAULT 0 comment 'time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci comment 'tbl_user';

DROP TABLE IF EXISTS `tbl_other`;
CREATE TABLE `tbl_other` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NULL DEFAULT '' comment 'string',
  `age` int unsigned NULL DEFAULT 0 comment 'integer',
  `school` varchar(64) NULL DEFAULT '' comment 'string',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci comment 'tbl_other';


implementation('com.fasterxml.jackson.module:jackson-module-kotlin')