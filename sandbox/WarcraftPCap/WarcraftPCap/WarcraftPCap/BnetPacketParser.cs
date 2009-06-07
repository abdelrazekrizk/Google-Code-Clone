using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SharpPcap;
using Asuah.Libs;

namespace Asuah.WarcraftPCap
{
    class BnetPacketParser
    {
        public PacketEvent[] Events = new PacketEvent[Constants.MAX_PACKET_ID];

        public BnetPacketParser()
        {
            for (int i = 0; i < Events.Length; i++)
            {
                Events[i] = new PacketEvent();
            }
        }

        public void OnPacketArrival(object sender, PcapCaptureEventArgs e)
        {
            if (e.Packet.Data.Length > 1)
            {
                BattleNetPacketID id = (BattleNetPacketID)e.Packet.Data[1];

                if ((int)id < Events.Length)
                {
                    Events[(int)id].DoEvent(sender, e);
                }
            }
        }
    }

    class PacketEvent
    {
        public delegate void PacketEventHandler(object sender, PcapCaptureEventArgs args);

        public event PacketEventHandler Event;

        public void DoEvent(Object sender, PcapCaptureEventArgs args){
            if (Event != null)
                Event(sender, args);
        }

        public void RegisterEvent(PacketEventHandler handler)
        {
            Event += new PacketEventHandler(handler);
        }
    }
}
