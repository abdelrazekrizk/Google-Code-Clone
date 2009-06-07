using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Asuah.WarcraftPCap
{
    interface IWarcraftPcapPlugin
    {
        void Register(BnetPacketParser parser);
    }
}
