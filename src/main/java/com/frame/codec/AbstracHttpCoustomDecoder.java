package com.frame.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @PackgeName:com.frame.codec
 * @ClassName:AbstracHttpCoustomRequest
 * @Auther: 马俊
 * @Date: 2019-08-02 09:17
 * @Description:
 */
public abstract class AbstracHttpCoustomDecoder<T> extends MessageToMessageDecoder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstracHttpCoustomDecoder.class);
    private Class<?> clazz;
    private boolean isPrint;
    private Charset charset;

    private AbstracHttpCoustomDecoder(){

    }
    protected AbstracHttpCoustomDecoder(Class<?> clazz,boolean isPrint){
        this(clazz,isPrint, CharsetUtil.UTF_8);
    }
    protected AbstracHttpCoustomDecoder(Class<?> clazz,boolean isPrint,Charset charset){
        this.clazz=clazz;
        this.isPrint=isPrint;
        this.charset=charset;
    }
    protected Charset getCharset(){
        return this.charset;
    }
    protected Object decode0(ChannelHandlerContext ctx, ByteBuf body) {
        String content = body.toString(charset);
        if (isPrint) {
            LOGGER.info("The body is : " + content);
        }
        return content;
    }
}
