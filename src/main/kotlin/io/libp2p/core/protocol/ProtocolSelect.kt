package io.libp2p.core.protocol

import io.libp2p.core.Libp2pException
import io.libp2p.core.events.ProtocolNegotiationFailed
import io.libp2p.core.events.ProtocolNegotiationSucceeded
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * Created by Anton Nashatyrev on 20.06.2019.
 */
class ProtocolSelect<TController>(val protocols: List<ProtocolBinding<TController>> = mutableListOf()) :
    ChannelInboundHandlerAdapter() {

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        when (evt) {
            is ProtocolNegotiationSucceeded -> {
                val protocolBinding = protocols.find { it.matcher.matches(evt.proto) }
                    ?: throw Libp2pException("Protocol negotiation failed: not supported protocol ${evt.proto}")
                ctx.pipeline().replace(this, "ProtocolBindingInitializer"
                    , protocolBinding.initializer(evt.proto).channelInitializer)
            }
            is ProtocolNegotiationFailed -> throw Libp2pException("ProtocolNegotiationFailed: $evt")
        }
        super.userEventTriggered(ctx, evt)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
        ctx.close()
    }
}