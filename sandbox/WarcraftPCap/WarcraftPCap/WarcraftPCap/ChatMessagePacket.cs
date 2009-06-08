using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SharpPcap;

namespace Asuah.WarcraftPCap
{
    class ChatMessagePacket
    {
        public ChatEventEvent[] Events = new ChatEventEvent[Constants.MAX_CHAT_EVENT_ID];

        public uint MessageID;
        public uint MessageLength;
        public ChatEventID EventID;
        public ulong Flags;
        public ulong Ping;
        public ulong IP;
        public ulong AcctNumber;
        public ulong RegAuth;
        public string Username;
        public string Text;

        public ChatMessagePacket(BnetPacketParser parser)
        {
            parser.Events[(int)BattleNetPacketID.ChatEvent].Event += new PacketEvent.PacketEventHandler(OnPacket);

            for (int i = 0; i < Events.Length; i++)
                Events[i] = new ChatEventEvent();

            //Events[(int)ChatEventID.Info].Event += new ChatEventEvent.ChatEventHandler((object o, PcapCaptureEventArgs e) => { Console.WriteLine("Stuff"); });
        }

        public void OnPacket(object sender, PcapCaptureEventArgs e)
        {
            PacketReader reader = new PacketReader(e.Packet.Data);

            reader.ReadHeader();
            MessageID       = reader.MessageID;
            MessageLength   = reader.MessageLength;
            EventID         = (ChatEventID)reader.ReadDWord();
            Flags           = reader.ReadDWord();
            Ping            = reader.ReadDWord();
            IP              = reader.ReadDWord();
            AcctNumber      = reader.ReadDWord();
            RegAuth         = reader.ReadDWord();
            Username        = reader.ReadString();
            Text            = reader.ReadString();

            Events[(int)EventID].DoEvent(this, e);
        }
    }

    class ChatEventEvent
    {
        public delegate void ChatEventHandler(object sender, PcapCaptureEventArgs packet);

        public event ChatEventHandler Event;

        public void DoEvent(Object sender, PcapCaptureEventArgs args)
        {
            if (Event != null)
                Event(sender, args);
        }

        public void RegisterEvent(ChatEventHandler handler)
        {
            Event += handler;
        }
    }
}
