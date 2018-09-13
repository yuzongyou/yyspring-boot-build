package com.duowan.common.redis.model;

/**
 * @author Arvin
 */
public abstract class AbstractRedisDef implements RedisDef {

    /**
     * 唯一标识符
     */
    protected String id;

    /**
     * 是否是主 Redis
     */
    protected boolean primary = false;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
