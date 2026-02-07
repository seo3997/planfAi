
-- 권한별 메뉴 매핑
CREATE TABLE `op_author_menu` (
  `MENU_ID` varchar(24) NOT NULL,
  `AUTHOR_ID` varchar(20) NOT NULL,
  `REGISTER_NO` int DEFAULT NULL,
  `REGIST_DT` datetime DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`,`AUTHOR_ID`)
);
