using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SharpPcap;

namespace Asuah.WarcraftPCap
{
    class ChatMessagePacket : IWarcraftPcapPlugin
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

        public ChatMessagePacket(BnetPacketParser parser)
        {
            parser.Events[(int)BattleNetPacketID.ChatEvent].Event += new PacketEvent.PacketEventHandler(OnPacket);


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


            return;

            switch (EventID)
            {
                case ChatEventID.Whisper:
                    Console.WriteLine("[W] From {0}: {1}", Username, Text);
                    break;
                case ChatEventID.WhisperSent:
                    Console.WriteLine("[W] To {0}: {1}", Username, Text);
                    break;
                case ChatEventID.Talk:
                    Console.WriteLine("[C] {0}: {1}", Username, Text);
                    break;
                case ChatEventID.Info:
                    Console.WriteLine("[I] Bnet: {0}", Text);
                    break;
                case ChatEventID.ShowUser:
                    Console.WriteLine("[i] {0} is in channel.", Username);
                    break;
                case ChatEventID.Channel:
                    Console.WriteLine("[#] channel changed: {0}", Text);
                    break;
                case ChatEventID.Emote:
                    Console.WriteLine("[e] {0} {1}", Username, Text);
                    break;
                case ChatEventID.Join:
                    Console.WriteLine("[+] {0} joined the channel.", Username);
                    break;
                case ChatEventID.Leave:
                    Console.WriteLine("[-] {0} left the channel.", Username);
                    break;
                default:
                    Console.WriteLine("Packet 0x{0:x2}", EventID);
                    break;
            }
        }

        public void Register(BnetPacketParser parser)
        {
        }

    }

    class ChatEventEvent
    {
        public delegate void ChatEventHandler(object sender, ChatMessagePacket packet);

        //public event 
    }
}
