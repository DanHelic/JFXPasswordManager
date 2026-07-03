package com.pwmanager.pwmanager;

import java.util.ArrayList;
import java.util.List;

import static com.pwmanager.pwmanager.Main.*;

public class Language {

    int whichLanguage; //which language
    //0 == german
    //1 == english
    //2 == other
    List<String> ger;
    List<String> eng;
    List<String> other;


    public void setLang(int langInt, LogInScreenPane pane){
        if(langInt==2||langInt==1||langInt==0) whichLanguage = langInt;
        else whichLanguage =0;
        pane.paintIt();
    }

    public String getLangString(){
        return getText(whichLanguage +4);
    }

    public Language(int def){
        whichLanguage = settingFile.langInt;
        ger=new ArrayList<String>();
        eng=new ArrayList<String>();
        other=new ArrayList<String>();

        //add German
        ger.add("Passwort:");
        ger.add("Einloggen");
        ger.add("Profil hinzufügen");
        ger.add("Profil Löschen");
        ger.add("Deutsch");
        ger.add("Englisch"); //5
        ger.add("GIBBYYY");
        ger.add("Einstellungen");
        ger.add("Sprache");
        ger.add("Zeit bis das Passwort nicht mehr kopiert werden kann");
        ger.add("Zeit bis Passwort aus Zwischenablage gelöscht wird"); //10
        ger.add("Sek.");
        ger.add("Profilname:");
        ger.add("Passwort:");
        ger.add("Passwort wiederholen:");
        ger.add("Passwörter stimmen nicht überein"); //15
        ger.add("Bitte alle Felder ausfüllen");
        ger.add("Profilname schon vergeben");
        ger.add("Profilbild hinzufügen");
        ger.add("Profilbild auswählen");
        ger.add("Ja, Löschen"); //20
        ger.add("Nein, nicht Löschen");
        ger.add("Unwiderruflich löschen?");
        ger.add("Passwort hinzufügen");
        ger.add("Passwort Löschen");
        ger.add("Passwort Bearbeiten"); //25
        ger.add("Ausloggen");
        ger.add("Benutzername / E-Mail");
        ger.add("Passwort");
        ger.add("Passwort wiederholen");
        ger.add("Zusatzinformationen"); //30
        ger.add("Passwort anzeigen");
        ger.add("Passwort generieren");
        ger.add("Bild auswählen");
        ger.add("Passwort hinzufügen");
        ger.add("Zwischenablage nach einfügen des Passworts wiederherstellen"); //35
        ger.add("Profil bearbeiten");
        ger.add("Passwort ändern");
        ger.add("Bild Ändern");
        ger.add("Altes Passwort");
        ger.add("Benutzername benötigt"); //40
        ger.add("Altes Passwort muss übereinstimmen");
        ger.add("Passwortlänge:   ");
        ger.add("Benutzername / E-Mail anzeigen wenn man eingelogged ist");
        ger.add("Status: ");
        ger.add("Zur Zeit nichts kopiert \n"); //45
        ger.add("Benutzername oder E-Mail kopiert \n");
        ger.add("Passwort kopiert \n");
        ger.add("Kopieren abbrechen");
        ger.add("Passwort ändern");
        ger.add("Zeit bis zum automatischen ausloggen (0=niemals)"); //50
        ger.add("Zeit bis zum Logout: ");
        ger.add("Automatischer Klicker");
        ger.add("Einfach");
        ger.add("Erweitert");
        ger.add("Dark Mode"); //55
        ger.add("Backups erstellen");
        ger.add("");
        ger.add("");
        ger.add("");






        //ad English
        eng.add("Password:");
        eng.add("Log In");
        eng.add("Add Profile");
        eng.add("Delete Profile");
        eng.add("German");
        eng.add("English"); //5
        eng.add("GIBBYYY");
        eng.add("Settings");
        eng.add("Language");
        eng.add("Time till password cant be copied anymore");
        eng.add("Time till password is getting deleted from clipboard"); //10
        eng.add("Sec.");
        eng.add("Profilename:");
        eng.add("Password:");
        eng.add("Repeat Password:");
        eng.add("Passwords do not match"); //15
        eng.add("Please fill all Fields");
        eng.add("Profilename already taken");
        eng.add("Add ProfilePicture");
        eng.add("Choose ProfilePicture");
        eng.add("Yes, delete"); //20
        eng.add("No, dont delete");
        eng.add("Delete irreversible?");
        eng.add("New Password");
        eng.add("Delete Password");
        eng.add("Edit Password"); //25
        eng.add("Log Out");
        eng.add("Username / E-Mail");
        eng.add("Password");
        eng.add("Repeat Password");
        eng.add("Additional Informations"); //30
        eng.add("Show Password");
        eng.add("Generate Password");
        eng.add("Choose a Picture");
        eng.add("Add Password");
        eng.add("Restore Clipboard after entering the Password"); //35
        eng.add("Edit Profile");
        eng.add("Change Password");
        eng.add("Change Picture");
        eng.add("Old Password");
        eng.add("Username needed"); //40
        eng.add("Old Password not the same");
        eng.add("Passwordlength:   ");
        eng.add("Show Username/E-Mail when logged in");
        eng.add("Status: ");
        eng.add("Nothing copied"); //45
        eng.add("Username or E-Mail copied");
        eng.add("Password copied");
        eng.add("Stop copy");
        eng.add("Change Password");
        eng.add("Time until auto logout (0=never)"); //50
        eng.add("Time until auto Logout: ");
        eng.add("Autoclicker");
        eng.add("Simple");
        eng.add("Extended");
        eng.add("Dark Mode"); //55
        eng.add("Create Backups");
        eng.add("");
        eng.add("");
        eng.add("");







        //other
        other.add("Passiwordi");
        other.add("inni loggi");
        other.add("mehr Profiiel");
        other.add("weniger Profiiel");
        other.add("Bier");
        other.add("Tea"); //5
        other.add("GIBBYYY");
        other.add("Sett the ing things");
        other.add("Sprich Deutsch du Hurensohn");
        other.add("Zeit bis Passiwordie nicht mehr kopierie");
        other.add("Zeit bis Passiwordi gelöschi würdi"); //10
        other.add("ms(Sek)");
        other.add("Profilerinsky:");
        other.add("Passiwordi:");
        other.add("Nochimali Passiwordi:");
        other.add("Passiwordi nix gleich"); //15
        other.add("Schreib überallwas hin man");
        other.add("Profil gibbed schon");
        other.add("Mit Bild alda");
        other.add("Such dir eins aus man");
        other.add("Lösch et"); //20
        other.add("Lösche et nit");
        other.add("Nix mehr rückgängig Wegmachen?");
        other.add("Ein neues Pw");
        other.add("Weg mit dem Pw");
        other.add("Pw ändern"); //25
        other.add("Raus hier");
        other.add("Username");
        other.add("Passiwordi");
        other.add("Passiwordi againi");
        other.add("Mehr Infos"); //30
        other.add("Zeig Pw");
        other.add("Mach mir eins Pw");
        other.add("Was fürn Bild");
        other.add("Passwort hinmachen");
        other.add("Mach dat Clipboard wieder hin"); //35
        other.add("Profil anders machen");
        other.add("Passiwordi anders machi");
        other.add("Andres Bild");
        other.add("dat alde pw");
        other.add("Brauchst Benutzername"); //40
        other.add("Falsche pw amk");
        other.add("Pw Länge:   ");
        other.add("Usernamerinskie oder so anzeigen wenn de drinne bischt");
        other.add("Statussi: ");
        other.add("Gornischd kobbiert"); //45
        other.add("Usernämchen koppiert");
        other.add("Patsword kopbiert");
        other.add("Kobpieren abprechen");
        other.add("Passiwordi ändereee");
        other.add("Eingeloggt sein Zeit so (0=nävaa)"); //50
        other.add("Zeit bis ausgeloggt: ");
        other.add("Automatisches Clickeroo");
        other.add("Goanz izzi");
        other.add("Meeehr");
        other.add("Dunkler Modus"); //55
        other.add("Backups ja oder nein man");
        other.add("");
        other.add("");
        other.add("");






    }


    public String getText(int i){
        if(whichLanguage ==0){
            return ger.get(i);
        }
        if(whichLanguage ==1){
            return eng.get(i);
        }
        if(whichLanguage ==2){
            return other.get(i);
        }
        return "";
    }
}
