using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Runtime.InteropServices;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading;
using System.Windows.Forms;
using Microsoft.DirectX.DirectInput;

namespace WC3Launcher
{
    [ComVisibleAttribute(false), System.Security.SuppressUnmanagedCodeSecurity()]
    public class DInputHook : IDisposable
    {
        [return: MarshalAs(UnmanagedType.Bool)]
        [DllImport("user32.dll", SetLastError = true)]
        static extern bool PostMessage(IntPtr hWnd, int Msg, System.Windows.Forms.Keys wParam, int lParam);

        IntPtr hWnd;
        Device keyboard;
        AutoResetEvent keyboardUpdated;
        ManualResetEvent appShutdown;
        Thread threadloop;
        List<Key> keyBuffer = new List<Key>();

        public delegate void KeyPressedHandler(List<Key> now, List<Key> buffer);

        public event KeyPressedHandler KeyStateChanged;

        public DInputHook(IntPtr hWnd)
        {
            this.hWnd = hWnd;
            keyboard = new Device(SystemGuid.Keyboard);
            keyboardUpdated = new AutoResetEvent(false);
            keyboard.SetCooperativeLevel(hWnd, CooperativeLevelFlags.NonExclusive | CooperativeLevelFlags.Background);
            keyboard.SetEventNotification(keyboardUpdated);

            appShutdown = new ManualResetEvent(false);

            threadloop = new Thread(new ThreadStart(ThreadFunction));
            keyboard.Acquire();
            threadloop.Start();
        }

        public void ThreadFunction()
        {
            WaitHandle[] handles = { keyboardUpdated, appShutdown };
            while (true)
            {
                switch (WaitHandle.WaitAny(handles))
                {
                    case 0:
                        UpdateKeyboardState();
                        break;
                    case 1:
                        return;
                        break;
                }
            }
        }

        public void UpdateKeyboardState()
        {
            string pressedKeys = "";
            List<Key> pressed = new List<Key>();
            foreach (Key k in keyboard.GetPressedKeys())
            {
                pressedKeys += k.ToString() + " ";
                pressed.Add(k);
            }
            
            // fire event here
            OnKeyStateChanged(pressed, keyBuffer);

            //Console.WriteLine(pressedKeys);
            keyBuffer = pressed;
        }

        public void OnKeyStateChanged(List<Key> now, List<Key> old)
        {
            if (KeyStateChanged != null)
                KeyStateChanged(now, old);
        }

        public void SendKey(Keys key)
        {
            PostMessage(hWnd, WM_KEYDOWN, key, 0);
        }

        #region Windows Key Status bindings
        private const Int32 WM_KEYDOWN = 0x100;
        private const Int32 WM_KEYUP = 0x101;
        #endregion

        #region IDisposable Members

        public void Dispose()
        {
            threadloop.Abort();
            keyboard.Unacquire();
        }

        #endregion
    }
}
