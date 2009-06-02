using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using SharpPcap;

namespace Asuah.WarcraftPCap
{
    class Program
    {
        
        static void Main(string[] args)
        {
            Console.WriteLine("SharpPcap Version: {0}", SharpPcap.Version.VersionString);

            PcapDevice device = AskForDevice();

            Console.WriteLine("Selected device description: {0:s}", device.Description);

            BnetPacketParser parser = new BnetPacketParser();

            device.OnPacketArrival += new Pcap.PacketArrivalEvent(parser.OnPacketArrival);

            device.Open();
            device.SetFilter("port 6112");

            RegisterPlugins(parser);

            device.StartCapture();
            Console.ReadLine();
            device.StopCapture();
        }

        public static void RegisterPlugins(BnetPacketParser parser)
        {
            new ChatMessagePacket(parser);
        }

        public static PcapDevice AskForDevice()
        {
            PcapDevice device = null;
            List<PcapDevice> devices = Pcap.GetAllDevices();

            // if we find a device with NVIDIA in the description, just assume we're using that.
            foreach (PcapDevice dev in devices)
                if (dev.Description.IndexOf("NVIDIA") >= 0)
                    device = dev;

            // now if we didnt find anything of the such, we'll let the user decide.
            while (device == null)
            {
                try
                {
                    for (int i = 0; i < devices.Count; i++)
                    {
                        device = devices[i];
                        Console.WriteLine(String.Format("{0:d}: {1:s}", i + 1, device.Description));
                    }
                    Console.WriteLine("Select device [1-{0:d}]:", devices.Count);
                    int index = Int32.Parse(Console.ReadLine());
                    device = devices[index - 1];
                }
                catch (Exception e)
                {
                    Console.WriteLine("Invalid selection. ({0:s})", e.Message);
                    device = null;
                }
            }

            return device;
        }
    }
}
