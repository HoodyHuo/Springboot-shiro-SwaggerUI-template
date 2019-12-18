package tech.hoody.platform.util

class ResponseData {

    def data

    Integer count

    /**
     * 20000 表示正常
     * 20001 表示业务错误
     *
     * 40001 缺少参数
     * 40002 参数类型错误
     *
     * 40301 权限不合法
     * 40302 账号已经另外登录
     * 40303 口令过期
     * 40304 未登录
     *
     * 40400 资源不存在
     *
     *
     */
    int code = 20000

    String msg

    @Override
    public String toString() {
        return "ResponseData{" +
                "data=" + data +
                ", count=" + count +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
