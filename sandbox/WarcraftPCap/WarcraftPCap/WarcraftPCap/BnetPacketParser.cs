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
        public PacketEventHolder[] eventHolders = new PacketEventHolder[Constants.MAX_PACKET_ID];

        public BnetPacketParser()
        {
            for (int i = 0; i < eventHolders.Length; i++)
            {
                eventHolders[i] = new PacketEventHolder();
            }
        }

        public void OnPacketArrival(object sender, PcapCaptureEventArgs e)
        {
            if (e.Packet.Data.Length > 1)
            {
                PacketIDConstants id = (PacketIDConstants)e.Packet.Data[1];
                /*
                const int headingSize = 15;
                string message = String.Format("{0}>", id.ToString());
                for (int i = 0; i < headingSize - message.ToString().Length; i++)
                    Console.Write(" ");
                Console.WriteLine(message); */

                if ((int)id < eventHolders.Length)
                {
                    eventHolders[(int)id].DoEvent(sender, e);
                }
            }
        }
    }

    class PacketEventHolder
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
