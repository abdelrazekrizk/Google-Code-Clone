class ImportSomeData < ActiveRecord::Migration
  def self.up
    down
    classes = Hash.new
    ["Mage", "Rogue", "Warrior", "Warlock", "Death Knight", "Priest", "Paladin", "Shaman", "Druid", "Hunter"].each do |c|
      classes[c] = CharacterClass.create :name => c
    end
    
    races = Hash.new
    ["Tauren", "Undead", "Troll", "Orc", "Blood Elf"].each do |r|
      races[r] = CharacterRace.create :name => r
    end
    
    zones = Hash.new
    ["Vault of Archavon", "Naxxramas", "Obsidian Sanctum", "Eye of Eternity", "Ulduar"].each do |z|
      zones[z] = Zone.create :name => z
    end
    
    bosses = Hash.new
    ["Archavon the Stone Watcher", "Emalon the Storm Watcher"].each do |b|
      bosses[b] = Boss.create :name => b, :zone => zones["Vault of Archavon"]
    end
    
    ["Anub'Rekhan", "Grand Widow Faerlina", "Maexxna", "Noth the Plaguebringer", "Heigan the Unclean", "Loatheb", "Instructor Razuvious", "Gothik the Harvester",
     "The Four Horsemen", "Patchwerk", "Grobbulus", "Gluth", "Thaddius", "Sapphiron", "Kel'Thuzad"].each do |b|
      bosses[b] = Boss.create :name => b, :zone => zones["Naxxramas"]
    end
     
    ["Sartharion", "Sartharion + one drake", "Sartharion + two drakes" , "Sartharion + three drakes"].each do |b|
      bosses[b] = Boss.create :name => b, :zone => zones["Obsidian Sanctum"]
    end
    
    ["Malygos"].each do |b|
      bosses[b] = Boss.create :name => b, :zone => zones["Eye of Eternity"]
    end
    
    ["Flame Leviathan", "Ignis the Furnace Master", "Razorscale", "XT-002 Deconstructor", "The Iron Council", "Kologarn", "Auriaya", "Mimiron", "Freya", "Thorim",
     "Hodir", "General Vezax", "Algalon the Observer", "Yogg-Saron" ]. each do |b|
       bosses[b] = Boss.create :name => b, :zone => zones["Ulduar"]
    end

    domia = Character.create :name => "Domia", :character_class => classes["Rogue"], :character_race => races["Undead"]
    adamant = Character.create :name => "Adamant", :character_class => classes["Warrior"], :character_race => races["Tauren"]
    malchiah = Character.create :name => "Malchiah", :character_class => classes["Priest"], :character_race => races["Blood Elf"]
    
    raid1 = Raid.create :note => "First raid ever", :boss => bosses["Kel'Thuzad"], :base_value => 100, :time => Time.now
    ar11 = AttendanceRecord.create :character_id => domia.id, :raid_id => raid1.id
    ar12 = AttendanceRecord.create :character_id => adamant.id, :raid_id => raid1.id
    
    decay1 = Decay.create :performed_on => Time.now + 10
    
    raid2 = Raid.create :note => "Post decay raid", :boss => bosses["Malygos"], :base_value => 125, :time => Time.now + 20
    ar21 = AttendanceRecord.create :character_id => domia.id, :raid_id => raid2.id
    ar22 = AttendanceRecord.create :character_id => malchiah.id, :raid_id => raid2.id
    
    decay2 = Decay.create :performed_on => Time.now - 20
    
    item1 = Item.create :name => "Calamity's Grasp", :wowitemid => 40383, :character => domia, :base_value => 1061, :raid => raid1
    
#    raid1.reload.destroy
    
    throw :lemmie_do_this_quick_damnit
    
  end

  def self.down
    CharacterClass.delete_all
    CharacterRace.delete_all
    Character.delete_all
    Boss.delete_all
    Zone.delete_all
    Raid.delete_all
    AttendanceRecord.delete_all
    Decay.delete_all
  end
end
