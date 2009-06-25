using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Windows.Forms;
using Asuah.WarcraftPCap;

namespace Asuah.WarcraftPCap.Plugins
{
    class AutoSpoof
    {
        protected DInputHook diHook;
        public AutoSpoof(BnetPacketParser parser, DInputHook diHook)
        {
            this.diHook = diHook;
            parser.chatMessageHandler.Events[(int)ChatEventID.Whisper].Event +=
                new ChatEventEvent.ChatEventHandler(OnWhisper);
        }

        public void OnWhisper(ChatMessagePacket packet, SharpPcap.PcapCaptureEventArgs e)
        {
            if (Regex.Match(packet.Text, "spoofcheck").Success)
            {
                Keys[] keys = {
                    Keys.Divide,
                    Keys.R,
                    Keys.Space,
                    Keys.S,
                    Keys.P,
                    Keys.O,
                    Keys.O,
                    Keys.F,
                    Keys.C,
                    Keys.H,
                    Keys.E,
                    Keys.C,
                    Keys.K
                };
                foreach(Keys key in keys){
                    diHook.SendKey(key);
                }
                Thread.Sleep(100);
                diHook.SendKey(Keys.Enter);
            }
        }
    }
}
