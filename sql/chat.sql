-- auto-generated definition
create table chat_session
(
    id             int auto_increment
        primary key,
    sessionId      varchar(20)       not null comment 'sessionId',
    createTime     datetime          null comment '会话创建时间',
    type           int(10) default 1 null comment '会话类型，1-用户对用户，2-群组',
    first_msg_time datetime          null comment '第一条消息发送时间',
    last_msg_time  datetime          null comment '最后一条消息发送时间',
    uid_hash       varchar(255)      not null comment 'uid 的混淆结果，简单混淆可以按一定规则（按字母顺序排序等方式）拼接两个用户的uid，不建议使用hash，会有一定概率出现哈希冲突，此字段主要用于加快会话查询速度',
    send_uid       varchar(255)      null comment '发起会话用户',
    receive_uid    varchar(255)      null comment '接收会话用户'
);

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会话管理', '2000', '1', 'session', 'xim/session/index', 1, 0, 'C', '0', '0', 'xim:session:list', '#', 'admin', sysdate(), '', null, '会话管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会话管理查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'xim:session:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会话管理新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'xim:session:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会话管理修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'xim:session:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会话管理删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'xim:session:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('会话管理导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'xim:session:export',       '#', 'admin', sysdate(), '', null, '');