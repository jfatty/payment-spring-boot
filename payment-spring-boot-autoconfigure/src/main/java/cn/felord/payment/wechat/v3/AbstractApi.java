package cn.felord.payment.wechat.v3;

import cn.felord.payment.PayException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.RequestEntity;
import org.springframework.util.Assert;

import java.net.URI;

/**
 * The type Abstract api.
 *
 * @author felord.cn
 * @since 1.0.0.RELEASE
 */
public abstract class AbstractApi {
    /**
     * The Mapper.
     */
    private final ObjectMapper mapper;
    /**
     * The Wechat pay client.
     */
    private final WechatPayClient wechatPayClient;
    /**
     * The Tenant id.
     */
    private final String tenantId;


    /**
     * Instantiates a new Abstract api.
     *
     * @param wechatPayClient the wechat pay client
     * @param tenantId        the tenant id
     */
    public AbstractApi(WechatPayClient wechatPayClient, String tenantId) {
        this.mapper = new ObjectMapper();
        applyObjectMapper(this.mapper);
        this.wechatPayClient = wechatPayClient;
        Assert.hasText(tenantId, "tenantId is required");
        if (!container().getTenantIds().contains(tenantId)) {
            throw new PayException("tenantId is not in wechatMetaContainer");
        }
        this.tenantId = tenantId;
    }

    /**
     * Apply object mapper.
     *
     * @param mapper the mapper
     */
    private void applyObjectMapper(ObjectMapper mapper) {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleModule module = new JavaTimeModule();
        mapper.registerModule(module);
    }


    /**
     * Gets mapper.
     *
     * @return the mapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }

    /**
     * Client wechat pay client.
     *
     * @return the wechat pay client
     */
    public WechatPayClient client() {
        return wechatPayClient;
    }

    /**
     * Tenant id string.
     *
     * @return the string
     */
    public String tenantId() {
        return tenantId;
    }

    /**
     * Container wechat meta container.
     *
     * @return the wechat meta container
     */
    public WechatMetaContainer container() {
        return wechatPayClient.signatureProvider().wechatMetaContainer();
    }

    /**
     * Wechat meta bean wechat meta bean.
     *
     * @return the wechat meta bean
     */
    public WechatMetaBean wechatMetaBean() {
        return container().getWechatMeta(tenantId);
    }

    /**
     * Post request entity.
     *
     * @param uri    the uri
     * @param params the params
     * @return the request entity
     */
    protected RequestEntity<?> Post(URI uri, Object params) {
        try {
            return RequestEntity.post(uri).header("Pay-TenantId", tenantId)
                    .body(mapper.writeValueAsString(params));
        } catch (JsonProcessingException e) {
            throw new PayException("wechat app pay json failed");
        }
    }

    /**
     * Get request entity.
     *
     * @param uri the uri
     * @return the request entity
     */
    protected RequestEntity<?> Get(URI uri) {
        return RequestEntity.get(uri).header("Pay-TenantId", tenantId)
                .build();
    }
}
