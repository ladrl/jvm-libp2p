/*
 * Copyright 2019 BLK Technologies Limited (web3labs.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.libp2p.core.mplex

import io.libp2p.core.Libp2pException
import io.libp2p.core.mux.MultistreamFrame
import io.libp2p.core.mux.MultistreamFrame.Flag.CLOSE
import io.libp2p.core.mux.MultistreamFrame.Flag.DATA
import io.libp2p.core.mux.MultistreamFrame.Flag.OPEN
import io.libp2p.core.mux.MultistreamFrame.Flag.RESET

/**
 * Contains all the permissible values for flags in the <code>mplex</code> protocol.
 */
object MplexFlags {
    const val NewStream = 0
    const val MessageReceiver = 1
    const val MessageInitiator = 2
    const val CloseReceiver = 3
    const val CloseInitiator = 4
    const val ResetReceiver = 5
    const val ResetInitiator = 6

    fun toAbstractFlag(mplexFlag: Int): MultistreamFrame.Flag =
        when(mplexFlag) {
            NewStream -> OPEN
            MessageReceiver, MessageInitiator -> DATA
            CloseReceiver, CloseInitiator -> CLOSE
            ResetReceiver, ResetInitiator -> RESET
            else -> throw Libp2pException("Unknown mplex flag: $mplexFlag")
        }

    fun toMplexFlag(abstractFlag: MultistreamFrame.Flag, initiator: Boolean): Int =
        when(abstractFlag) {
            OPEN -> NewStream
            DATA -> if (initiator) MessageInitiator else MessageReceiver
            CLOSE -> if (initiator) CloseInitiator else CloseReceiver
            RESET -> if (initiator) ResetInitiator else ResetReceiver
        }

}