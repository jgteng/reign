DROP TABLE IF EXISTS `reign_task`;
CREATE TABLE `reign_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `main_script` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `task_rule` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `status` int(2) NOT NULL,
  `last_run_time` datetime DEFAULT NULL,
  `next_time` datetime DEFAULT NULL,
  `description` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime DEFAULT NULL,
  `disabled` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_name_unique` (`task_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `reign_task_node`;
CREATE TABLE `reign_task_node` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `node_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `ip` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


DROP TABLE IF EXISTS `reign_task_node_group`;
CREATE TABLE `reign_task_node_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `reign_task_node_group_rel`;
CREATE TABLE `reign_task_node_group_rel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `node_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `reign_task_run_history`;
CREATE TABLE `reign_task_run_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` bigint(20) NOT NULL,
  `run_bach_num` int(11) NOT NULL,
  `task_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NOT NULL,
  `status` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for reign_script
-- ----------------------------
DROP TABLE IF EXISTS `reign_script`;
CREATE TABLE `reign_script` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scpt_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `scpt_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `scpt_path` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `scpt_version` int(11) NOT NULL,
  `description` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime NOT NULL,
  `modified` datetime DEFAULT NULL,
  `deleted` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

- ----------------------------
-- Table structure for reign_script_old_version
-- ----------------------------
DROP TABLE IF EXISTS `reign_script_old_version`;
CREATE TABLE `reign_script_old_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scpt_id` bigint(20) NOT NULL,
  `scpt_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `scpt_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `scpt_path` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `scpt_version` int(11) NOT NULL,
  `description` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` datetime NOT NULL,
  `deleted` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

