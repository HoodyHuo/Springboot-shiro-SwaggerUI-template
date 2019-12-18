package tech.hoody.platform.util

class SaltAndencryptPwd {
    private String salt
    private String encryptPassword

    SaltAndencryptPwd(salt, encryptPwd) {
        this.salt = salt
        this.encryptPassword = encryptPwd
    }

    String getSalt() {
        return salt
    }

    void setSalt(String salt) {
        this.salt = salt
    }

    String getEncryptPassword() {
        return encryptPassword
    }

    void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword
    }
}
