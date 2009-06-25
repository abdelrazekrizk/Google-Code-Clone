using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SharpPcap;

namespace Asuah.WarcraftPCap
{
    class ChatMessagePacketHandler
    {
        public ChatEventEvent[] Events = new ChatEventEvent[Constants.MAX_CHAT_EVENT_ID];

        public ChatMessagePacketHandler(BnetPacketParser parser)
        {
            parser.Events[(int)BattleNetPacketID.ChatEvent].Event += new PacketEvent.PacketEventHandler(OnPacket);

            for (int i = 0; i < Events.Length; i++)
                Events[i] = new ChatEventEvent();

            //Events[(int)ChatEventID.Info].Event += new ChatEventEvent.ChatEventHandler((object o, PcapCaptureEventArgs e) => { Console.WriteLine("Stuff"); });
        }

        public void OnPacket(object sender, PcapCaptureEventArgs e)
        {
            ChatMessagePacket packet = new ChatMessagePacket();
            PacketReader reader = new PacketReader(e.Packet.Data);

            reader.ReadHeader();
            packet.MessageID     = reader.MessageID;
            packet.MessageLength = reader.MessageLength;
            packet.EventID       = (ChatEventID)reader.ReadDWord();
            packet.Flags         = reader.ReadDWord();
            packet.Ping          = reader.ReadDWord();
            packet.IP            = reader.ReadDWord();
            packet.AcctNumber    = reader.ReadDWord();
            packet.RegAuth       = reader.ReadDWord();
            packet.Username      = reader.ReadString();
            packet.Text          = reader.ReadString();

            Events[(int)packet.EventID].DoEvent(packet, e);
        }
    }

    class ChatMessagePacket
    {
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
    }

    class ChatEventEvent
    {
        public delegate void ChatEventHandler(ChatMessagePacket packet, PcapCaptureEventArgs args);

        public event ChatEventHandler Event;

        public void DoEvent(ChatMessagePacket packet, PcapCaptureEventArgs args)
        {
            if (Event != null)
                Event(packet, args);
        }

        public void RegisterEvent(ChatEventHandler handler)
        {
            Event += handler;
        }
    }
}
