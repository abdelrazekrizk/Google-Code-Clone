using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Asuah.WarcraftPCap
{
    class PacketReader
    {
        private int index = 0;

        byte[] data;

        public uint MessageID;
        public uint MessageLength;

        public PacketReader(byte[] data)
        {
            this.data = data;
        }

        public byte ReadByte()
        {
            return data[index++];
        }

        public uint ReadWord()
        {
            uint word = 0;
            for (int i = 1; i >= 0; i--)
            {
                word = word * (uint)0x100 + data[index + i];
            }
            index += 2;
            return word;
        }

        public ulong ReadDWord()
        {
            ulong dword = 0;
            for (int i = 3; i >= 0; i--)
            {
                dword = dword * (ulong)0x100 + data[index + i];
            }
            index += 4;
            return dword;
        }

        public string ReadString()
        {
            string str = "";
            while(data[index] != 0x00)
                str += (char)ReadByte();
            index++;
            return str;
        }

        public bool ReadHeader()
        {
            if (ReadByte() == 0xFF)
            {
                MessageID = ReadByte();
                MessageLength = ReadWord();
            }
            return true;
        }


    }
}
