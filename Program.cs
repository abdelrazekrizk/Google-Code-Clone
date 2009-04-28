using System;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using System.Text;
using System.Diagnostics;
using System.IO;
using System.Text.RegularExpressions;
using System.Threading;
using System.Collections;
using System.Windows.Forms;
using Microsoft.DirectX.DirectInput;

namespace WC3Launcher
{
    class Program
    {
        #region User32.dll function imports
        [DllImport("user32.dll")]
        static extern bool MoveWindow(IntPtr hWnd, int X, int Y, int nWidth, int nHeight, bool bRepaint);

        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        static extern bool IsWindowVisible(IntPtr hWnd);

        [DllImport("user32.dll")]
        static extern bool ClipCursor(ref RECT lpRect);

        [DllImport("user32.dll")]
        static extern bool GetClipCursor(out RECT lpRect);

        [DllImport("user32.dll")]
        [return: MarshalAs(UnmanagedType.Bool)]
        static extern bool GetWindowRect(IntPtr hWnd, out RECT lpRect);

        [DllImport("user32.dll")]
        static extern int GetSystemMetrics(SystemMetric smIndex);

        [DllImport("user32.dll")]
        static extern IntPtr GetForegroundWindow();
        #endregion

        #region attributes
        #region Contents of the ini file
        static string ini = "wc3launcher.ini";
        static string iniContents;
        #endregion

        #region System metrics
        static int BorderHeightMetric = GetSystemMetrics(SystemMetric.SM_CYSIZEFRAME);
        static int BorderWidthMetric = GetSystemMetrics(SystemMetric.SM_CXSIZEFRAME);
        static int BorderMenuBarMetric = GetSystemMetrics(SystemMetric.SM_CYMENU);
        #endregion

        #region globals
        static int x, y, height, width, step;
        static float aspect;
        static IntPtr wc3;
        static bool locked = true;
        static RECT oldCursorClipRect;
        #endregion
        #endregion

        static void Main(string[] args)
        {
            ReadIniFile();
            string path = GetIniValue("path") + "\\war3.exe";
            aspect = ParseAspect(GetIniValue("aspect"));
            height = ParseInt(GetIniValue("height"));
            step = ParseInt(GetIniValue("step"));
            int[] coords = ParseCoords(GetIniValue("coords"));
            x = coords[0]; // damn all, no multiple assignment php-style (list($x, $y) = coords;)
            y = coords[1];

            if (File.Exists(path))
            {
                GetClipCursor(out oldCursorClipRect);
                wc3 = StartWC3(path);

                DInputHook input = new DInputHook(wc3);
                input.KeyStateChanged += new DInputHook.KeyPressedHandler(OnKeyPressed);
                WhileWC3Running();
                SaveSettings();
                input.Dispose();
            }
            else
            {
                MessageBox.Show("Path is not correctly set in the ini file.", "WC3Launcher");
            }
        }

        #region Key event handlers
        public static void OnKeyPressed(List<Key> now, List<Key> old)
        {
            //printkeypresses(now, old);
            if (now.Contains(Key.LeftControl) && now.Contains(Key.LeftShift))
            {
                UpdateWC3WindowMetrics();
                if (JustPressed(Key.Equals, now, old))
                {
                    height += step;
                    width = (int)(height * aspect);
                }
                else if (JustPressed(Key.Minus, now, old))
                {
                    height -= step;
                    width = (int)(height * aspect);
                }
                else if (JustPressed(Key.Left, now, old))
                    x -= step;
                else if (JustPressed(Key.Right, now, old))
                    x += step;
                else if (JustPressed(Key.Up, now, old))
                    y -= step;
                else if (JustPressed(Key.Down, now, old))
                    y += step;
                MoveWindow(wc3, x, y, width, height, true);
            }
            if (JustPressed(Key.F7, now, old))
            {
                ToggleMouseLock();
            }
        }

        public static void ToggleMouseLock()
        {
            locked ^= true;
            if (!locked)
                ClipCursor(ref oldCursorClipRect);
            else
                ConstrainMouseToWC3();
        }

        private static bool JustPressed(Key k, List<Key> now, List<Key> old)
        {
            return now.Contains(k) && !old.Contains(k);
        }

        private static void printkeypresses(List<Key> now, List<Key> old)
        {
            foreach (Key k in now)
            {
                if (!old.Contains(k))
                {
                    Console.WriteLine("+{0}", k);
                }
            }
            foreach (Key k in old)
            {
                if (!now.Contains(k))
                {
                    Console.WriteLine("-{0}", k);
                }
            }
        }
        #endregion

        #region Mouse Constraining
        public static void ConstrainMouseToWC3()
        {
            IntPtr windowHandle = GetWC3WindowHandle();
            if (windowHandle != IntPtr.Zero && IsWindowVisible(windowHandle))
            {
                RECT windowRect = GetWC3WindowRect();
                windowRect.Left += BorderWidthMetric;
                windowRect.Right -= BorderWidthMetric;
                windowRect.Top = windowRect.Top + BorderMenuBarMetric + BorderHeightMetric; // 28
                windowRect.Bottom -= BorderHeightMetric;
                ClipCursor(ref windowRect);
            }
        }

        public static RECT GetWC3WindowRect()
        {
            RECT windowRect;
            GetWindowRect(GetWC3WindowHandle(), out windowRect);
            return windowRect;
        }

        public static void UpdateWC3WindowMetrics()
        {
            RECT windowRect = GetWC3WindowRect();
            x = windowRect.Left;
            y = windowRect.Top;
            height = windowRect.height();
            width = (int)(height * aspect);
        }

        private static void UnconstrainMouse(RECT oldBounds)
        {
            ClipCursor(ref oldBounds);
        }
        #endregion

        #region Warcraft 3 Running
        private static void WaitForWC3Start()
        {
            while (Process.GetProcessesByName("war3").Length == 0) ;
        }

        public static IntPtr StartWC3(string path)
        {
            Process.Start(@path, "-window");

            while (true)
            {
                IntPtr windowHandle = GetWC3WindowHandle();
                if (windowHandle != IntPtr.Zero && IsWindowVisible(windowHandle))
                {
                    width = (int)(height * aspect);
                    MoveWindow(windowHandle, x, y, width, height, true);
                    return windowHandle;
                }
            }
        }

        private static void WhileWC3Running()
        {
            while (true)
            {
                Process[] processes = Process.GetProcessesByName("war3");
                Process wc3;
                if (processes.Length == 0)
                    return;
                else
                    wc3 = processes[0];
                IntPtr windowHandle = wc3.MainWindowHandle;
                if (GetForegroundWindow() == windowHandle)
                {
                    UpdateWC3WindowMetrics();
                    if(locked)
                        ConstrainMouseToWC3();
                }
                Thread.Sleep(100);
            }
        }

        public static IntPtr GetWC3WindowHandle()
        {
            Process[] processes = Process.GetProcessesByName("war3");
            if (processes.Length > 0)
                return processes[0].MainWindowHandle;
            else
                return IntPtr.Zero;
        }
        #endregion

        #region ini management
        public static string GetIniValue(string iniTag)
        {
            Regex rex = new Regex(String.Format("{0}=(.*)\r\n", iniTag));
            Match result = rex.Match(iniContents);
            if (result.Success)
            {
                return result.Result("$1");
            } 
            else
            {
                return "";
            }
        }

        public static int ParseInt(string i)
        {
            if (i != "")
            {
                return Int32.Parse(i);
            }
            else
            {
                return 0;
            }
        }

        private static void ReadIniFile()
        {
            StreamReader reader = File.OpenText(ini);
            Regex rexComment = new Regex("#(.*)");
            while (!reader.EndOfStream)
            {
                String line = reader.ReadLine();
                if (line != "" && !rexComment.Match(line).Success) // if the line does not match the form for a commented line
                {
                    iniContents += line + "\r\n";
                }
            }
            reader.Close();
            //Console.Write(iniContents);
        }

        public static void SaveSettings()
        {
            StreamReader reader = File.OpenText(ini);
            String contents = reader.ReadToEnd();
            reader.Close();

            contents = Regex.Replace(contents, "height=(.*)\r\n", String.Format("height={0}\r\n", height));
            contents = Regex.Replace(contents, "coords=(.*)\r\n", String.Format("coords={0},{1}\r\n", x, y));

            StreamWriter writer = new StreamWriter(ini);
            writer.Write(contents);
            writer.Close();
        }

        private static float ParseAspect(string aspect)
        { 
            // the input string should be in the form {width}:{height}, ie 4:3
            Regex rex = new Regex("^([0-9]*):([0-9]*)$");
            Match match = rex.Match(aspect);
            if (match.Success)
            {
                float widthScalar = float.Parse(match.Result("$1"));
                float heightScalar = float.Parse(match.Result("$2"));
                return widthScalar / heightScalar;
            }
            else // fallback
            {
                MessageBox.Show("Aspect not set properly in ini, defaulting to 4:3", "WC3Launcher");
                return 4.0f / 3.0f;
            }
        }

        public static int[] ParseCoords(string coords)
        {
            Regex rex = new Regex("^([0-9]*),([0-9]*)$");
            Match match = rex.Match(coords);
            if (match.Success)
            {
                int x = Int32.Parse(match.Result("$1"));
                int y = Int32.Parse(match.Result("$2"));
                return new int[] { x, y };
            }
            else // fallback
            {
                MessageBox.Show("Coordinates not set properly in ini, defaulting to (0, 0)", "WC3Launcher");
                return new int[] { 0, 0 };
            }
        }
        #endregion

        #region User32.dll Structures/Enumerations
        #region RECT struct
        [Serializable, StructLayout(LayoutKind.Sequential)]
        public struct RECT
        {
            public int Left;
            public int Top;
            public int Right;
            public int Bottom;

            public override string ToString()
            {
                return String.Format("RECT: @({0},{1}) dim:{2}x{3}", Left, Top, width(), height());
            }
            public int height()
            {
                return Bottom - Top;
            }
            public int width()
            {
                return Right - Left;
            }
        }
        #endregion

        #region SystemMetric enum
        public enum SystemMetric : int
        {
            /// <summary>
            ///  Width of the screen of the primary display monitor, in pixels. This is the same values obtained by calling GetDeviceCaps as follows: GetDeviceCaps( hdcPrimaryMonitor, HORZRES).
            /// </summary>
            SM_CXSCREEN = 0,
            /// <summary>
            /// Height of the screen of the primary display monitor, in pixels. This is the same values obtained by calling GetDeviceCaps as follows: GetDeviceCaps( hdcPrimaryMonitor, VERTRES).
            /// </summary>
            SM_CYSCREEN = 1,
            /// <summary>
            /// Width of a horizontal scroll bar, in pixels.
            /// </summary>
            SM_CYVSCROLL = 2,
            /// <summary>
            /// Height of a horizontal scroll bar, in pixels.
            /// </summary>
            SM_CXVSCROLL = 3,
            /// <summary>
            /// Height of a caption area, in pixels.
            /// </summary>
            SM_CYCAPTION = 4,
            /// <summary>
            /// Width of a window border, in pixels. This is equivalent to the SM_CXEDGE value for windows with the 3-D look.
            /// </summary>
            SM_CXBORDER = 5,
            /// <summary>
            /// Height of a window border, in pixels. This is equivalent to the SM_CYEDGE value for windows with the 3-D look.
            /// </summary>
            SM_CYBORDER = 6,
            /// <summary>
            /// Thickness of the frame around the perimeter of a window that has a caption but is not sizable, in pixels. SM_CXFIXEDFRAME is the height of the horizontal border and SM_CYFIXEDFRAME is the width of the vertical border.
            /// </summary>
            SM_CXDLGFRAME = 7,
            /// <summary>
            /// Thickness of the frame around the perimeter of a window that has a caption but is not sizable, in pixels. SM_CXFIXEDFRAME is the height of the horizontal border and SM_CYFIXEDFRAME is the width of the vertical border.
            /// </summary>
            SM_CYDLGFRAME = 8,
            /// <summary>
            /// Height of the thumb box in a vertical scroll bar, in pixels
            /// </summary>
            SM_CYVTHUMB = 9,
            /// <summary>
            /// Width of the thumb box in a horizontal scroll bar, in pixels.
            /// </summary>
            SM_CXHTHUMB = 10,
            /// <summary>
            /// Default width of an icon, in pixels. The LoadIcon function can load only icons with the dimensions specified by SM_CXICON and SM_CYICON
            /// </summary>
            SM_CXICON = 11,
            /// <summary>
            /// Default height of an icon, in pixels. The LoadIcon function can load only icons with the dimensions SM_CXICON and SM_CYICON.
            /// </summary>
            SM_CYICON = 12,
            /// <summary>
            /// Width of a cursor, in pixels. The system cannot create cursors of other sizes.
            /// </summary>
            SM_CXCURSOR = 13,
            /// <summary>
            /// Height of a cursor, in pixels. The system cannot create cursors of other sizes.
            /// </summary>
            SM_CYCURSOR = 14,
            /// <summary>
            /// Height of a single-line menu bar, in pixels.
            /// </summary>
            SM_CYMENU = 15,
            /// <summary>
            /// Width of the client area for a full-screen window on the primary display monitor, in pixels. To get the coordinates of the portion of the screen not obscured by the system taskbar or by application desktop toolbars, call the SystemParametersInfo function with the SPI_GETWORKAREA value.
            /// </summary>
            SM_CXFULLSCREEN = 16,
            /// <summary>
            /// Height of the client area for a full-screen window on the primary display monitor, in pixels. To get the coordinates of the portion of the screen not obscured by the system taskbar or by application desktop toolbars, call the SystemParametersInfo function with the SPI_GETWORKAREA value.
            /// </summary>
            SM_CYFULLSCREEN = 17,
            /// <summary>
            /// For double byte character set versions of the system, this is the height of the Kanji window at the bottom of the screen, in pixels
            /// </summary>
            SM_CYKANJIWINDOW = 18,
            /// <summary>
            /// Nonzero if a mouse with a wheel is installed; zero otherwise
            /// </summary>
            SM_MOUSEWHEELPRESENT = 75,
            /// <summary>
            /// Height of the arrow bitmap on a vertical scroll bar, in pixels.
            /// </summary>
            SM_CYHSCROLL = 20,
            /// <summary>
            /// Width of the arrow bitmap on a horizontal scroll bar, in pixels.
            /// </summary>
            SM_CXHSCROLL = 21,
            /// <summary>
            /// Nonzero if the debug version of User.exe is installed; zero otherwise.
            /// </summary>
            SM_DEBUG = 22,
            /// <summary>
            /// Nonzero if the left and right mouse buttons are reversed; zero otherwise.
            /// </summary>
            SM_SWAPBUTTON = 23,
            /// <summary>
            /// Reserved for future use
            /// </summary>
            SM_RESERVED1 = 24,
            /// <summary>
            /// Reserved for future use
            /// </summary>
            SM_RESERVED2 = 25,
            /// <summary>
            /// Reserved for future use
            /// </summary>
            SM_RESERVED3 = 26,
            /// <summary>
            /// Reserved for future use
            /// </summary>
            SM_RESERVED4 = 27,
            /// <summary>
            /// Minimum width of a window, in pixels.
            /// </summary>
            SM_CXMIN = 28,
            /// <summary>
            /// Minimum height of a window, in pixels.
            /// </summary>
            SM_CYMIN = 29,
            /// <summary>
            /// Width of a button in a window's caption or title bar, in pixels.
            /// </summary>
            SM_CXSIZE = 30,
            /// <summary>
            /// Height of a button in a window's caption or title bar, in pixels.
            /// </summary>
            SM_CYSIZE = 31,
            /// <summary>
            /// Thickness of the sizing border around the perimeter of a window that can be resized, in pixels. SM_CXSIZEFRAME is the width of the horizontal border, and SM_CYSIZEFRAME is the height of the vertical border.
            /// </summary>
            SM_CXFRAME = 32,
            /// <summary>
            /// Thickness of the sizing border around the perimeter of a window that can be resized, in pixels. SM_CXSIZEFRAME is the width of the horizontal border, and SM_CYSIZEFRAME is the height of the vertical border.
            /// </summary>
            SM_CYFRAME = 33,
            /// <summary>
            /// Minimum tracking width of a window, in pixels. The user cannot drag the window frame to a size smaller than these dimensions. A window can override this value by processing the WM_GETMINMAXINFO message.
            /// </summary>
            SM_CXMINTRACK = 34,
            /// <summary>
            /// Minimum tracking height of a window, in pixels. The user cannot drag the window frame to a size smaller than these dimensions. A window can override this value by processing the WM_GETMINMAXINFO message
            /// </summary>
            SM_CYMINTRACK = 35,
            /// <summary>
            /// Width of the rectangle around the location of a first click in a double-click sequence, in pixels. The second click must occur within the rectangle defined by SM_CXDOUBLECLK and SM_CYDOUBLECLK for the system to consider the two clicks a double-click
            /// </summary>
            SM_CXDOUBLECLK = 36,
            /// <summary>
            /// Height of the rectangle around the location of a first click in a double-click sequence, in pixels. The second click must occur within the rectangle defined by SM_CXDOUBLECLK and SM_CYDOUBLECLK for the system to consider the two clicks a double-click. (The two clicks must also occur within a specified time.)
            /// </summary>
            SM_CYDOUBLECLK = 37,
            /// <summary>
            /// Width of a grid cell for items in large icon view, in pixels. Each item fits into a rectangle of size SM_CXICONSPACING by SM_CYICONSPACING when arranged. This value is always greater than or equal to SM_CXICON
            /// </summary>
            SM_CXICONSPACING = 38,
            /// <summary>
            /// Height of a grid cell for items in large icon view, in pixels. Each item fits into a rectangle of size SM_CXICONSPACING by SM_CYICONSPACING when arranged. This value is always greater than or equal to SM_CYICON.
            /// </summary>
            SM_CYICONSPACING = 39,
            /// <summary>
            /// Nonzero if drop-down menus are right-aligned with the corresponding menu-bar item; zero if the menus are left-aligned.
            /// </summary>
            SM_MENUDROPALIGNMENT = 40,
            /// <summary>
            /// Nonzero if the Microsoft Windows for Pen computing extensions are installed; zero otherwise.
            /// </summary>
            SM_PENWINDOWS = 41,
            /// <summary>
            /// Nonzero if User32.dll supports DBCS; zero otherwise. (WinMe/95/98): Unicode
            /// </summary>
            SM_DBCSENABLED = 42,
            /// <summary>
            /// Number of buttons on mouse, or zero if no mouse is installed.
            /// </summary>
            SM_CMOUSEBUTTONS = 43,
            /// <summary>
            /// Identical Values Changed After Windows NT 4.0  
            /// </summary>
            SM_CXFIXEDFRAME = SM_CXDLGFRAME,
            /// <summary>
            /// Identical Values Changed After Windows NT 4.0
            /// </summary>
            SM_CYFIXEDFRAME = SM_CYDLGFRAME,
            /// <summary>
            /// Identical Values Changed After Windows NT 4.0
            /// </summary>
            SM_CXSIZEFRAME = SM_CXFRAME,
            /// <summary>
            /// Identical Values Changed After Windows NT 4.0
            /// </summary>
            SM_CYSIZEFRAME = SM_CYFRAME,
            /// <summary>
            /// Nonzero if security is present; zero otherwise.
            /// </summary>
            SM_SECURE = 44,
            /// <summary>
            /// Width of a 3-D border, in pixels. This is the 3-D counterpart of SM_CXBORDER
            /// </summary>
            SM_CXEDGE = 45,
            /// <summary>
            /// Height of a 3-D border, in pixels. This is the 3-D counterpart of SM_CYBORDER
            /// </summary>
            SM_CYEDGE = 46,
            /// <summary>
            /// Width of a grid cell for a minimized window, in pixels. Each minimized window fits into a rectangle this size when arranged. This value is always greater than or equal to SM_CXMINIMIZED.
            /// </summary>
            SM_CXMINSPACING = 47,
            /// <summary>
            /// Height of a grid cell for a minimized window, in pixels. Each minimized window fits into a rectangle this size when arranged. This value is always greater than or equal to SM_CYMINIMIZED.
            /// </summary>
            SM_CYMINSPACING = 48,
            /// <summary>
            /// Recommended width of a small icon, in pixels. Small icons typically appear in window captions and in small icon view
            /// </summary>
            SM_CXSMICON = 49,
            /// <summary>
            /// Recommended height of a small icon, in pixels. Small icons typically appear in window captions and in small icon view.
            /// </summary>
            SM_CYSMICON = 50,
            /// <summary>
            /// Height of a small caption, in pixels
            /// </summary>
            SM_CYSMCAPTION = 51,
            /// <summary>
            /// Width of small caption buttons, in pixels.
            /// </summary>
            SM_CXSMSIZE = 52,
            /// <summary>
            /// Height of small caption buttons, in pixels.
            /// </summary>
            SM_CYSMSIZE = 53,
            /// <summary>
            /// Width of menu bar buttons, such as the child window close button used in the multiple document interface, in pixels.
            /// </summary>
            SM_CXMENUSIZE = 54,
            /// <summary>
            /// Height of menu bar buttons, such as the child window close button used in the multiple document interface, in pixels.
            /// </summary>
            SM_CYMENUSIZE = 55,
            /// <summary>
            /// Flags specifying how the system arranged minimized windows
            /// </summary>
            SM_ARRANGE = 56,
            /// <summary>
            /// Width of a minimized window, in pixels.
            /// </summary>
            SM_CXMINIMIZED = 57,
            /// <summary>
            /// Height of a minimized window, in pixels.
            /// </summary>
            SM_CYMINIMIZED = 58,
            /// <summary>
            /// Default maximum width of a window that has a caption and sizing borders, in pixels. This metric refers to the entire desktop. The user cannot drag the window frame to a size larger than these dimensions. A window can override this value by processing the WM_GETMINMAXINFO message.
            /// </summary>
            SM_CXMAXTRACK = 59,
            /// <summary>
            /// Default maximum height of a window that has a caption and sizing borders, in pixels. This metric refers to the entire desktop. The user cannot drag the window frame to a size larger than these dimensions. A window can override this value by processing the WM_GETMINMAXINFO message.
            /// </summary>
            SM_CYMAXTRACK = 60,
            /// <summary>
            /// Default width, in pixels, of a maximized top-level window on the primary display monitor.
            /// </summary>
            SM_CXMAXIMIZED = 61,
            /// <summary>
            /// Default height, in pixels, of a maximized top-level window on the primary display monitor.
            /// </summary>
            SM_CYMAXIMIZED = 62,
            /// <summary>
            /// Least significant bit is set if a network is present; otherwise, it is cleared. The other bits are reserved for future use
            /// </summary>
            SM_NETWORK = 63,
            /// <summary>
            /// Value that specifies how the system was started: 0-normal, 1-failsafe, 2-failsafe /w net
            /// </summary>
            SM_CLEANBOOT = 67,
            /// <summary>
            /// Width of a rectangle centered on a drag point to allow for limited movement of the mouse pointer before a drag operation begins, in pixels.
            /// </summary>
            SM_CXDRAG = 68,
            /// <summary>
            /// Height of a rectangle centered on a drag point to allow for limited movement of the mouse pointer before a drag operation begins. This value is in pixels. It allows the user to click and release the mouse button easily without unintentionally starting a drag operation.
            /// </summary>
            SM_CYDRAG = 69,
            /// <summary>
            /// Nonzero if the user requires an application to present information visually in situations where it would otherwise present the information only in audible form; zero otherwise.
            /// </summary>
            SM_SHOWSOUNDS = 70,
            /// <summary>
            /// Width of the default menu check-mark bitmap, in pixels.
            /// </summary>
            SM_CXMENUCHECK = 71,
            /// <summary>
            /// Height of the default menu check-mark bitmap, in pixels.
            /// </summary>
            SM_CYMENUCHECK = 72,
            /// <summary>
            /// Nonzero if the computer has a low-end (slow) processor; zero otherwise
            /// </summary>
            SM_SLOWMACHINE = 73,
            /// <summary>
            /// Nonzero if the system is enabled for Hebrew and Arabic languages, zero if not.
            /// </summary>
            SM_MIDEASTENABLED = 74,
            /// <summary>
            /// Nonzero if a mouse is installed; zero otherwise. This value is rarely zero, because of support for virtual mice and because some systems detect the presence of the port instead of the presence of a mouse.
            /// </summary>
            SM_MOUSEPRESENT = 19,
            /// <summary>
            /// Windows 2000 (v5.0+) Coordinate of the top of the virtual screen
            /// </summary>
            SM_XVIRTUALSCREEN = 76,
            /// <summary>
            /// Windows 2000 (v5.0+) Coordinate of the left of the virtual screen
            /// </summary>
            SM_YVIRTUALSCREEN = 77,
            /// <summary>
            /// Windows 2000 (v5.0+) Width of the virtual screen
            /// </summary>
            SM_CXVIRTUALSCREEN = 78,
            /// <summary>
            /// Windows 2000 (v5.0+) Height of the virtual screen
            /// </summary>
            SM_CYVIRTUALSCREEN = 79,
            /// <summary>
            /// Number of display monitors on the desktop
            /// </summary>
            SM_CMONITORS = 80,
            /// <summary>
            /// Windows XP (v5.1+) Nonzero if all the display monitors have the same color format, zero otherwise. Note that two displays can have the same bit depth, but different color formats. For example, the red, green, and blue pixels can be encoded with different numbers of bits, or those bits can be located in different places in a pixel's color value.
            /// </summary>
            SM_SAMEDISPLAYFORMAT = 81,
            /// <summary>
            /// Windows XP (v5.1+) Nonzero if Input Method Manager/Input Method Editor features are enabled; zero otherwise
            /// </summary>
            SM_IMMENABLED = 82,
            /// <summary>
            /// Windows XP (v5.1+) Width of the left and right edges of the focus rectangle drawn by DrawFocusRect. This value is in pixels.
            /// </summary>
            SM_CXFOCUSBORDER = 83,
            /// <summary>
            /// Windows XP (v5.1+) Height of the top and bottom edges of the focus rectangle drawn by DrawFocusRect. This value is in pixels.
            /// </summary>
            SM_CYFOCUSBORDER = 84,
            /// <summary>
            /// Nonzero if the current operating system is the Windows XP Tablet PC edition, zero if not.
            /// </summary>
            SM_TABLETPC = 86,
            /// <summary>
            /// Nonzero if the current operating system is the Windows XP, Media Center Edition, zero if not.
            /// </summary>
            SM_MEDIACENTER = 87,
            /// <summary>
            /// Metrics Other
            /// </summary>
            SM_CMETRICS_OTHER = 76,
            /// <summary>
            /// Metrics Windows 2000
            /// </summary>
            SM_CMETRICS_2000 = 83,
            /// <summary>
            /// Metrics Windows NT
            /// </summary>
            SM_CMETRICS_NT = 88,
            /// <summary>
            /// Windows XP (v5.1+) This system metric is used in a Terminal Services environment. If the calling process is associated with a Terminal Services client session, the return value is nonzero. If the calling process is associated with the Terminal Server console session, the return value is zero. The console session is not necessarily the physical console - see WTSGetActiveConsoleSessionId for more information.
            /// </summary>
            SM_REMOTESESSION = 0x1000,
            /// <summary>
            /// Windows XP (v5.1+) Nonzero if the current session is shutting down; zero otherwise
            /// </summary>
            SM_SHUTTINGDOWN = 0x2000,
            /// <summary>
            /// Windows XP (v5.1+) This system metric is used in a Terminal Services environment. Its value is nonzero if the current session is remotely controlled; zero otherwise
            /// </summary>
            SM_REMOTECONTROL = 0x2001,
        }
        #endregion
        #endregion
    }

    #region Input hooking through DirectX

    [ComVisibleAttribute(false), System.Security.SuppressUnmanagedCodeSecurity()]
    public class DInputHook : IDisposable
    {
        Device keyboard, mouse;
        AutoResetEvent keyboardUpdated;
        AutoResetEvent mouseUpdated;
        ManualResetEvent appShutdown;
        Thread threadloop;
        List<Key> keyBuffer = new List<Key>();

        public delegate void KeyPressedHandler(List<Key> now, List<Key> buffer);

        public event KeyPressedHandler KeyStateChanged;

        public DInputHook(IntPtr wc3)
        {
            keyboard = new Device(SystemGuid.Keyboard);
            keyboardUpdated = new AutoResetEvent(false);
            keyboard.SetCooperativeLevel(wc3, CooperativeLevelFlags.NonExclusive | CooperativeLevelFlags.Background);
            keyboard.SetEventNotification(keyboardUpdated);

            //mouse = new Device(SystemGuid.Mouse);
            mouseUpdated = new AutoResetEvent(false);
            //mouse.SetCooperativeLevel(wc3, CooperativeLevelFlags.NonExclusive | CooperativeLevelFlags.Background);
            //mouse.SetEventNotification(mouseUpdated);

            appShutdown = new ManualResetEvent(false);
            
            threadloop = new Thread(new ThreadStart(ThreadFunction));
            threadloop.Start();
            keyboard.Acquire();
            //mouse.Acquire();
        }

        public void ThreadFunction()
        {
            WaitHandle[] handles = { keyboardUpdated, mouseUpdated, appShutdown };
            while (true)
            {
                int index = WaitHandle.WaitAny(handles);
                if (index == 0)
                {
                    UpdateKeyboardState();
                }
                if (index == 1)
                {
                    UpdateMouseState();
                }
                else if (index == 2)
                {
                    return;
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

        public void UpdateMouseState()
        {
            string pressedKeys = "";
            byte[] buttons = mouse.CurrentMouseState.GetMouseButtons();
            for (int i = 0; i < buttons.Length; i++)
            {
                if ((buttons[i] & 128) == 128)
                {
                    pressedKeys += String.Format("MOUSE{0} ", i);
                }
            }
            //Console.WriteLine(mouse.CurrentMouseState.Z + " " + pressedKeys);
        }

        #region IDisposable Members

        public void Dispose()
        {
            threadloop.Abort();
            keyboard.Unacquire();
        }

        #endregion
    }
    #endregion

    #region Input hooking through User32.dll functions - deprecated
    /*
    // A lot of this was ripped/derived from http://www.codeproject.com/KB/system/CSLLKeyboard.aspx (thanks to Emma Burrows for the code and tutorial)
    class InputHook : IDisposable
    {
        IntPtr keyboardHookId = IntPtr.Zero;
        IntPtr mouseHookId = IntPtr.Zero;
        HookHandlerDelegate keyboardHook, mouseHook;

        #region Callbacks and event handlers
        delegate IntPtr HookHandlerDelegate(int nCode, IntPtr wParam, ref KBDLLHOOKSTRUCT lParam);
        delegate void KeyboardHookEventHandler(KeyboardHookEventArgs e);
        event KeyboardHookEventHandler KeyIntercepted;

        public void OnKeyIntercepted(KeyboardHookEventArgs e)
        {
            if (KeyIntercepted != null)
                KeyIntercepted(e);
        }
        public class KeyboardHookEventArgs : System.EventArgs
        {

            private string keyName;
            private int keyCode;

            /// <summary>
            /// The name of the key that was pressed.
            /// </summary>
            public string KeyName
            {
                get { return keyName; }
            }

            /// <summary>
            /// The virtual key code of the key that was pressed.
            /// </summary>
            public int KeyCode
            {
                get { return keyCode; }
            }

            public KeyboardHookEventArgs(int evtKeyCode)
            {
                keyName = ((Keys)evtKeyCode).ToString();
                keyCode = evtKeyCode;
            }

        }
        #endregion

        #region Internal structures returned by windows functions
        internal struct KBDLLHOOKSTRUCT
        {
            public int vkCode;
            int scanCode;
            public int flags;
            int time;
            int dwExtraInfo;
        }
        #endregion
        
        private const int WH_KEYBOARD_LL = 13;
        private const int WH_MOUSE_LL = 14;

        #region Constructor
        public InputHook()
        {
            keyboardHook = new HookHandlerDelegate(KeyboardCallback); 
            mouseHook = new HookHandlerDelegate(MouseCallback);
            using (Process curProcess = Process.GetCurrentProcess())
            {
                using (ProcessModule curModule = curProcess.MainModule)
                {
                    //curModule.
                    //keyboardHookId = SetWindowsHookEx(WH_KEYBOARD_LL, keyboardHook, GetModuleHandle(curModule.ModuleName), 0);
                    keyboardHookId = SetWindowsHookEx(WH_KEYBOARD_LL, keyboardHook, IntPtr.Zero, 0);
                    //mouseHookId = SetWindowsHookEx(WH_MOUSE_LL, mouseHook, GetModuleHandle(curModule.ModuleName), 0);
                }
            }
            Console.WriteLine("Hooks installed.");
        }
        #endregion

        private IntPtr KeyboardCallback(int nCode, IntPtr wParam, ref KBDLLHOOKSTRUCT lParam)
        {
            OnKeyIntercepted(new KeyboardHookEventArgs(lParam.vkCode));
            Console.WriteLine("Key intercepted: {0}", (Keys)lParam.vkCode);
            return CallNextHookEx(keyboardHookId, nCode, wParam, ref lParam);
        }

        private IntPtr MouseCallback(int nCode, IntPtr wParam, ref KBDLLHOOKSTRUCT lParam)
        {
            return CallNextHookEx(mouseHookId, nCode, wParam, ref lParam);
        }

        #region Windows API dll imports
        [DllImport("kernel32.dll")]
        static extern IntPtr GetModuleHandle(string module);

        [DllImport("user32.dll", SetLastError = true)]
        static extern IntPtr SetWindowsHookEx(int idHook, HookHandlerDelegate callback, IntPtr hMod, uint dwThreadId);

        [DllImport("user32.dll")]
        static extern bool UnhookWindowsHookEx(IntPtr hhk);

        [DllImport("user32.dll")]
        static extern IntPtr CallNextHookEx(IntPtr hhk, int nCode, IntPtr wParam, ref KBDLLHOOKSTRUCT lParam);

        [DllImport("user32.dll")]
        static extern short GetKeyState(int keyCode);
        #endregion

        #region IDisposable Members
        public void Dispose()
        {
            UnhookWindowsHookEx(keyboardHookId);
            UnhookWindowsHookEx(mouseHookId);
            Console.WriteLine("Hooks uninstalled.");
        }
        #endregion
    }
     */
    #endregion
}
