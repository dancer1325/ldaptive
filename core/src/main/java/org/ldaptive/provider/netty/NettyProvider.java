/* See LICENSE for licensing and NOTICE for copyright. */
package org.ldaptive.provider.netty;

import java.util.Map;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import org.ldaptive.Connection;
import org.ldaptive.ConnectionConfig;
import org.ldaptive.provider.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates netty connections.
 *
 * @author  Middleware Services
 */
public class NettyProvider implements Provider
{

  /** Logger for this class. */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /** Channel type. */
  private final Class<? extends Channel> channelType;

  /** Event loop group for I/O, must support the channel type. */
  private final EventLoopGroup ioWorkerGroup;

  /** Event loop group for message handling. */
  private final EventLoopGroup messageWorkerGroup;

  /** Override channel options. */
  private final Map<ChannelOption, Object> channelOptions;


  /**
   * Creates a new netty provider.
   *
   * @param  type  of channel
   * @param  ioGroup  event loop group to handle I/O
   */
  public NettyProvider(final Class<? extends Channel> type, final EventLoopGroup ioGroup)
  {
    this(type, ioGroup, null, null);
  }


  /**
   * Creates a new netty provider.
   *
   * @param  type  of channel
   * @param  ioGroup  event loop group to handle I/O
   * @param  messageGroup  event loop group to handle inbound messages, can be null
   * @param  options  netty channel options
   */
  public NettyProvider(
    final Class<? extends Channel> type,
    final EventLoopGroup ioGroup,
    final EventLoopGroup messageGroup,
    final Map<ChannelOption, Object> options)
  {
    channelType = type;
    ioWorkerGroup = ioGroup;
    messageWorkerGroup = messageGroup;
    channelOptions = options;
  }


  @Override
  public Connection create(final ConnectionConfig cc)
  {
    return new NettyConnection(cc, channelType, ioWorkerGroup, messageWorkerGroup, channelOptions);
  }


  @Override
  public void close()
  {
    try {
      ioWorkerGroup.shutdownGracefully();
    } catch (Exception e) {
      logger.warn("Error shutting down the I/O worker group", e);
    }
    if (messageWorkerGroup != null) {
      try {
        messageWorkerGroup.shutdownGracefully();
      } catch (Exception e) {
        logger.warn("Error shutting down the message worker group", e);
      }
    }
  }
}
