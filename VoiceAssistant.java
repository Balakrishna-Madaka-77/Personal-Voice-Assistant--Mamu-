package Mavenproject.Mavenproo;

import java.io.IOException;
import java.util.Scanner;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class VoiceAssistant {

    // --- Text-to-Speech ---
    public static void speak(String text) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            voice.speak(text);
            voice.deallocate();
        } else {
            System.out.println("Voice not found.");
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        Configuration config = new Configuration();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a configuration: (1) Default, (2) Alternate, (3) Custom, (4) Video, (5) Extra, (6) Wifi, (7) Settings");
        int choice = scanner.nextInt();
        scanner.close();

        // Base acoustic model
        config.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");

        // Select dictionary + language model
        if (choice == 1) {
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/0604.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/0604.lm");
        } else if (choice == 2) {
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/0823.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/0823.lm");
        } else if (choice == 3) {
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/6944.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/6944.lm");
        } else if (choice == 4) {
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/6054.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/6054.lm");
        } else if (choice == 5) {
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/6389.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/6389.lm");
        } else if (choice == 6) {
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/0383.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/0383.lm");
        } else if (choice == 7) {
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/8088.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/8088.lm");
        } else {
            System.out.println("Invalid choice. Using Default configuration.");
            config.setDictionaryPath("src/main/java/Mavenproject/Mavenproo/0604.dic");
            config.setLanguageModelPath("src/main/java/Mavenproject/Mavenproo/0604.lm");
        }

        try {
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(config);
            recognizer.startRecognition(true);

            // --- MAMU wakes up immediately ---
            boolean isAwake = true; // start awake
            speak("Hello, I am MAMU. How can I help you?");
            System.out.println("MAMU awakened and listening...");

            while (true) {
                SpeechResult result = recognizer.getResult();
                if (result == null) {
                    continue;
                }

                String utterance = result.getHypothesis();
                if (utterance == null || utterance.trim().isEmpty()) {
                    continue;
                }
                System.out.println("Heard: " + utterance);

                // --- Sleep / Wake control ---
                if (utterance.equalsIgnoreCase("go to sleep")
                        || utterance.equalsIgnoreCase("sleep")
                        || utterance.equalsIgnoreCase("sleep mamu")) {
                    isAwake = false;
                    speak("Going to sleep. Say 'Hai Mamu' to wake me.");
                    continue;
                }

                if (!isAwake) {
                    if (utterance.equalsIgnoreCase("Hai Mamu") || utterance.equalsIgnoreCase("Hi Mamu")) {
                        isAwake = true;
                        speak("Yes, I am listening.");
                    } else {
                        System.out.println("MAMU is sleeping... (waiting for 'Hai Mamu')");
                    }
                    continue;
                }

                // If awake and user says wake word again, just acknowledge and continue
                if (utterance.equalsIgnoreCase("Hai Mamu") || utterance.equalsIgnoreCase("Hi Mamu")) {
                    speak("Yes, I am listening.");
                    continue;
                }

                // --- Command handling (single else-if chain) ---
                String voiceCommand = utterance;

                // Browser & Application Control
                if (voiceCommand.equalsIgnoreCase("Open Chrome")) {
                    Runtime.getRuntime().exec("cmd.exe /c start chrome");
                    speak("Opening Chrome Browser");
                } else if (voiceCommand.equalsIgnoreCase("Close Chrome")) {
                    Runtime.getRuntime().exec("cmd.exe /c TASKKILL /IM chrome.exe /F");
                    speak("Closing Chrome Browser");
                } else if (voiceCommand.equalsIgnoreCase("Open Youtube")) {
                    Runtime.getRuntime().exec("cmd.exe /c start \"\" \"https://www.youtube.com/\"");
                    speak("Opening Youtube");
                } else if (voiceCommand.equalsIgnoreCase("Close Youtube")) {
                    Runtime.getRuntime().exec("cmd.exe /c TASKKILL /IM chrome.exe /F");
                    speak("Closing Youtube");
                } else if (voiceCommand.equalsIgnoreCase("Pause YouTube")) {
                    // Requires your YouTubeControl implementation
                    YouTubeControl.controlYouTube("pause youtube");
                    speak("Pausing Youtube");
                } else if (voiceCommand.equalsIgnoreCase("Resume YouTube")) {
                    YouTubeControl.controlYouTube("resume youtube");
                    speak("Resuming Youtube");
                } else if (voiceCommand.equalsIgnoreCase("Rewind YouTube")) {
                    YouTubeControl.controlYouTube("rewind youtube");
                    speak("Rewinding Youtube");
                } else if (voiceCommand.equalsIgnoreCase("Forward YouTube")) {
                    YouTubeControl.controlYouTube("forward youtube");
                    speak("Forwarding");
                } else if (voiceCommand.equalsIgnoreCase("Mute YouTube")) {
                    YouTubeControl.controlYouTube("mute youtube");
                    speak("Muted");
                } else if (voiceCommand.equalsIgnoreCase("Unmute YouTube")) {
                    YouTubeControl.controlYouTube("unmute youtube");
                    speak("Unmuted");
                } else if (voiceCommand.equalsIgnoreCase("Full Screen")) {
                    YouTubeControl.controlYouTube("full screen youtube");
                    speak("Enlarging");
                } else if (voiceCommand.equalsIgnoreCase("Exit Full Screen")) {
                    YouTubeControl.controlYouTube("exit full screen youtube");
                    speak("Minimizing");

                // Music Control
                } else if (voiceCommand.equalsIgnoreCase("Play Music")) {
                    String musicPath = "C:\\Users\\ASUS\\Downloads\\[iSongs.info] 03 - Gaaju Bomma.mp3";
                    Runtime.getRuntime().exec("cmd.exe /c start wmplayer \"" + musicPath + "\"");
                    speak("Playing Music");
                } else if (voiceCommand.equalsIgnoreCase("Stop Music")) {
                    Runtime.getRuntime().exec("cmd.exe /c TASKKILL /IM wmplayer.exe /F");
                    speak("Closing Music");

                // Local Video Control
                } else if (voiceCommand.equalsIgnoreCase("Play Video")) {
                    Runtime.getRuntime().exec("cmd.exe /c start wmplayer \"C:\\Users\\ASUS\\Downloads\\videoplayback.mp4\"");
                    speak("Playing Video");
                } else if (voiceCommand.equalsIgnoreCase("Stop Video")) {
                    Runtime.getRuntime().exec("cmd.exe /c TASKKILL /IM wmplayer.exe /F");
                    speak("I'm closing video");

                // Battery
                } else if (voiceCommand.equalsIgnoreCase("battery")) {
                    Process process = Runtime.getRuntime().exec("powershell.exe (Get-WmiObject -Class Win32_Battery).EstimatedChargeRemaining");
                    Scanner scanner1 = new Scanner(process.getInputStream());
                    if (scanner1.hasNextInt()) {
                        int battery = scanner1.nextInt();
                        speak("Battery percentage is " + battery + " percent.");
                    } else {
                        speak("Battery status not available.");
                    }
                    scanner1.close();

                // Brightness
                } else if (voiceCommand.equalsIgnoreCase("brightness")) {
                    speak("Please say increase brightness or decrease brightness.");
                } else if (voiceCommand.equalsIgnoreCase("increase")) {
                    Runtime.getRuntime().exec("powershell.exe (Get-WmiObject -Namespace root/WMI -Class WmiMonitorBrightnessMethods).WmiSetBrightness(1,80)");
                    speak("Increasing brightness.");
                } else if (voiceCommand.equalsIgnoreCase("decrease")) {
                    Runtime.getRuntime().exec("powershell.exe (Get-WmiObject -Namespace root/WMI -Class WmiMonitorBrightnessMethods).WmiSetBrightness(1,40)");
                    speak("Decreasing brightness.");

                // Power
                } else if (voiceCommand.equalsIgnoreCase("restart")) {
                    Runtime.getRuntime().exec("shutdown -r -t 05");
                    speak("Restarting PC in 05 seconds.");
                } else if (voiceCommand.equalsIgnoreCase("shutdown")) {
                    Runtime.getRuntime().exec("shutdown -s -t 05");
                    speak("Shutting down PC in 05 seconds.");

                // Network & Bluetooth
                } else if (voiceCommand.equalsIgnoreCase("wifi on")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"netsh interface set interface 'Wi-Fi' enabled\"");
                    speak("WiFi turned on");
                } else if (voiceCommand.equalsIgnoreCase("wifi off")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"netsh interface set interface 'Wi-Fi' disabled\"");
                    speak("WiFi turned off");
                } else if (voiceCommand.equalsIgnoreCase("Bluetooth on")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"devcon enable 'USB\\VID_0A12&PID_0001'\"");
                    speak("Bluetooth turned on");
                } else if (voiceCommand.equalsIgnoreCase("Bluetooth off")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"devcon disable 'USB\\VID_0A12&PID_0001'\"");
                    speak("Bluetooth turned off");

                // Time & Date
                } else if (voiceCommand.equalsIgnoreCase("time")) {
                    String time = java.time.LocalTime.now().toString();
                    speak("Current time is " + time);
                } else if (voiceCommand.equalsIgnoreCase("date")) {
                    String date = java.time.LocalDate.now().toString();
                    speak("Today's date is " + date);

                // Volume
                } else if (voiceCommand.equalsIgnoreCase("volume increase")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"nircmd.exe changesysvolume 2000\"");
                    speak("Volume increased");
                } else if (voiceCommand.equalsIgnoreCase("Volume decrease")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"nircmd.exe changesysvolume -2000\"");
                    speak("Volume decreased");

                // Battery Saver
                } else if (voiceCommand.equalsIgnoreCase("battery saver on")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"powercfg /setactive a1841308-3541-4fab-bc81-f71556f20b4a\"");
                    speak("Battery saver mode enabled");
                } else if (voiceCommand.equalsIgnoreCase("battery saver off")) {
                    Runtime.getRuntime().exec("powershell.exe -Command \"powercfg /setactive 381b4222-f694-41f0-9685-ff5bb260df2e\"");
                    speak("Battery saver mode disabled");

                // System Settings
                } else if (voiceCommand.equalsIgnoreCase("open settings")) {
                    Runtime.getRuntime().exec("cmd /c start ms-settings:");
                    speak("Opening settings");
                } else if (voiceCommand.equalsIgnoreCase("close settings")) {
                    Runtime.getRuntime().exec("taskkill /F /IM SystemSettings.exe");
                    speak("Closing Settings");

                // Camera
                } else if (voiceCommand.equalsIgnoreCase("open camera")) {
                    try {
                        Runtime.getRuntime().exec("powershell.exe Start-Process \"microsoft.windows.camera:\"");
                        speak("Opening Cam");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (voiceCommand.equalsIgnoreCase("close camera")) {
                    Runtime.getRuntime().exec("taskkill /F /IM WindowsCamera.exe");
                    speak("Closing Cam");

                // MS Office Hub / Word / Excel / Notepad
                } else if (voiceCommand.equalsIgnoreCase("open ms office")) {
                    Runtime.getRuntime().exec("cmd /c start winword");
                    speak("MS office is opening");
                } else if (voiceCommand.equalsIgnoreCase("close ms office")) {
                    Runtime.getRuntime().exec("taskkill /F /IM winword.exe");
                    speak("Closing MS office");
                } else if (voiceCommand.equalsIgnoreCase("open ms excel")) {
                    Runtime.getRuntime().exec("cmd /c start excel");
                    speak("Opening ms excel");
                } else if (voiceCommand.equalsIgnoreCase("close excel")) {
                    Runtime.getRuntime().exec("taskkill /F /IM excel.exe");
                    speak("Closing ms excel");
                } else if (voiceCommand.equalsIgnoreCase("open notepad")) {
                    Runtime.getRuntime().exec("notepad");
                    speak("Opening Notepad");
                } else if (voiceCommand.equalsIgnoreCase("close notepad")) {
                    Runtime.getRuntime().exec("taskkill /F /IM notepad.exe");
                    speak("Closing notepad");
                } else if (voiceCommand.equalsIgnoreCase("open ms word")) {
                    Runtime.getRuntime().exec("cmd /c start winword");
                    speak("Opening ms word");
                } else if (voiceCommand.equalsIgnoreCase("close ms word")) {
                    Runtime.getRuntime().exec("taskkill /F /IM winword.exe");
                    speak("Closing ms word");

                } else {
                    System.out.println("Command not recognized.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
