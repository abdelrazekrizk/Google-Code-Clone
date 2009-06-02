using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using SharpPcap;

namespace Asuah.WarcraftPCap
{
    class ChatMessagePacket : IWarcraftPcapPlugin
    {
        const uint EID_SHOWUSER = 0x01;
        const uint EID_JOIN = 0x02;
        const uint EID_LEAVE = 0x03;
        const uint EID_WHISPER = 0x04;
        const uint EID_TALK = 0x05;
        const uint EID_BROADCAST = 0x06;
        const uint EID_CHANNEL = 0x07;
        const uint EID_USERFLAGS = 0x09;
        const uint EID_WHISPERSENT = 0x0A;
        const uint EID_CHANNELFULL = 0x0D;
        const uint EID_CHANNELDOESNOTEXIST = 0x0E;
        const uint EID_CHANNELRESTRICTED = 0x0F;
        const uint EID_INFO = 0x12;
        const uint EID_ERROR = 0x13;
        const uint EID_EMOTE = 0x17;

        uint MessageID;
        uint MessageLength;
        ulong EventID;
        ulong Flags;
        ulong Ping;
        ulong IP;
        ulong AcctNumber;
        ulong RegAuth;
        string Username;
        string Text;

        public ChatMessagePacket(BnetPacketParser parser)
        {
            parser.eventHolders[(int)PacketIDConstants.ChatEvent].Event += new PacketEventHolder.PacketEventHandler(OnPacket);
        }

        public void OnPacket(object sender, PcapCaptureEventArgs e)
        {
            PacketReader reader = new PacketReader(e.Packet.Data);
            reader.ReadHeader();
            MessageID = reader.MessageID;
            MessageLength = reader.MessageLength;
            EventID = reader.ReadDWord();
            Flags = reader.ReadDWord();
            Ping = reader.ReadDWord();
            IP = reader.ReadDWord();
            AcctNumber = reader.ReadDWord();
            RegAuth = reader.ReadDWord();
            Username = reader.ReadString();
            Text = reader.ReadString();

            switch (EventID)
            {
                case EID_WHISPER:
                    Console.WriteLine("[W] From {0}: {1}", Username, Text);
                    break;
                case EID_WHISPERSENT:
                    Console.WriteLine("[W] To {0}: {1}", Username, Text);
                    break;
                case EID_TALK:
                    Console.WriteLine("[C] {0}: {1}", Username, Text);
                    break;
                case EID_INFO:
                    Console.WriteLine("[I] Bnet: {0}", Text);
                    break;
                case EID_SHOWUSER:
                    Console.WriteLine("[i] {0} is in channel.", Username);
                    break;
                case EID_CHANNEL:
                    Console.WriteLine("[#] channel changed: {0}", Text);
                    break;
                case EID_EMOTE:
                    Console.WriteLine("[e] {0} {1}", Username, Text);
                    break;
                case EID_JOIN:
                    Console.WriteLine("[+] {0} joined the channel.", Username);
                    break;
                case EID_LEAVE:
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
}
