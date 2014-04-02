package cn.yidukeji.bean;

/**
 * Created with IntelliJ IDEA.
 * User: ZXW
 * Date: 14-4-2
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
public class AccessUser {
    private String accessKeyId;
    private String secretKey;
    private String name;
    private String description;
    private Integer level;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
