package com.frame.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @PackgeName:com.frame.codec
 * @ClassName:AbstracHttpCoustomEncoder
 * @Auther: 马俊
 * @Date: 2019-08-02 09:23
 * @Description:
 */
public abstract class AbstracHttpCoustomEncoder<T> extends MessageToMessageEncoder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstracHttpCoustomEncoder.class);
    private boolean isPrint;
    private Charset charset;
    private AbstracHttpCoustomEncoder(){
        this(false);
    }
    protected AbstracHttpCoustomEncoder(boolean isPrint){
        this(isPrint, CharsetUtil.UTF_8);
    }
    protected AbstracHttpCoustomEncoder(boolean isPrint,Charset charset){
        this.isPrint=isPrint;
        this.charset=charset;
    }
    protected ByteBuf encode0(ChannelHandlerContext ctx, String body) {
//        String str = JSON.toJSONString(body);
        if(isPrint)
            LOGGER.info(body);
        ByteBuf encodeBuf = Unpooled.copiedBuffer(body, charset);
        return encodeBuf;
    }
}
