using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Threading;
using System.Text.RegularExpressions;
using Asuah.WarcraftPCap;
using System.Text;

namespace Asuah.WarcraftPCap.Plugins
{
    class THRGamesList
    {
        public bool inTHRChannel = false;
        THRGamesListForm form;
        public THRGamesList(BnetPacketParser parser, DInputHook diHook)
        {
            parser.chatMessageHandler.Events[(int)ChatEventID.Emote].Event +=
                new ChatEventEvent.ChatEventHandler(OnEmote);
            parser.chatMessageHandler.Events[(int)ChatEventID.Channel].Event +=
                new ChatEventEvent.ChatEventHandler(OnJoin);
            form = new THRGamesListForm();
            Thread formThread = new Thread(new ThreadStart(OpenForm));
            formThread.Start();
        }

        public void OpenForm()
        {
            form.ShowDialog();
        }

        public void OnEmote(ChatMessagePacket packet, SharpPcap.PcapCaptureEventArgs e)
        {
            if (packet.Username == "throneit.com")
            {
                List<string> games = new List<string>();
                MatchCollection matches = Regex.Matches(packet.Text, "(thr-[a-z]*[0-9]*)");
                foreach (Match match in matches)
                {
                    games.Add(match.Captures[0].Value);
                    Console.WriteLine(match.Captures[0].Value);
                }
                Console.WriteLine("------------------------------------");
            }
        }

        public void OnJoin(ChatMessagePacket packet, SharpPcap.PcapCaptureEventArgs e)
        {
            if (packet.Text.ToLower() == "clan thr")
            {
                inTHRChannel = true;
            }
            else
            {
                inTHRChannel = false;
            }
        }
    }

    class THRGamesListForm : Form
    {
        [DllImport("user32.dll")]
        public static extern int GetKeyboardState(byte[] lpKeyState);
        const int VK_ALT = 0x12;
        const int VK_LEFTALT = 0xA4;
        const int VK_RIGHTALT = 0xA5;

        public THRGamesListForm() : base()
        {
            this.FormBorderStyle = FormBorderStyle.None;
            //this.ControlBox = false;
            this.MouseDown += new MouseEventHandler(THRGamesListForm_MouseDown);
        }

        void THRGamesListForm_MouseDown(object sender, MouseEventArgs e)
        {
            
            if (e.Button == MouseButtons.Left)
            {
                byte[] keyboardState = new byte[256];
                GetKeyboardState(keyboardState);
                byte altstate = keyboardState[VK_ALT];
                if (altstate != 0 && altstate != 2)
                {
                    MessageBox.Show("Rawr");
                }
            }
        }
    }
}
