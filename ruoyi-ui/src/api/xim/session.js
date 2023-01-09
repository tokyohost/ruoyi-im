import request from '@/utils/request'

// 查询会话管理列表
export function listSession(query) {
  return request({
    url: '/xim/session/list',
    method: 'get',
    params: query
  })
}

// 查询会话管理详细
export function getSession(id) {
  return request({
    url: '/xim/session/' + id,
    method: 'get'
  })
}

// 新增会话管理
export function addSession(data) {
  return request({
    url: '/xim/session',
    method: 'post',
    data: data
  })
}

// 修改会话管理
export function updateSession(data) {
  return request({
    url: '/xim/session',
    method: 'put',
    data: data
  })
}

// 删除会话管理
export function delSession(id) {
  return request({
    url: '/xim/session/' + id,
    method: 'delete'
  })
}
