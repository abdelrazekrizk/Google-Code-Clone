class EqdkpXmlParserController < ApplicationController
  require 'rexml/document'
  def self.parse(xml)
    doc, chars, loot = REXML::Document.new(xml), [], []
    doc.elements.each('RaidInfo/PlayerInfos/*') do |k|
      name = k.elements['name'].text
      charclass = class_map(k.elements['class'].text)
      charrace = race_map(k.elements['race'].text)
      
      chars << Hash[:name => name, :charclass => charclass, :charrace => charrace]
    end
    
    chars.sort! {|a, b| a[:name] <=> b[:name]}
    
    boss = doc.elements['RaidInfo/BossKills/key1/name']
    if(!boss.nil?)
      boss = boss.text
    end
    
    date = doc.elements['RaidInfo/end'].text
    
    doc.elements.each('RaidInfo/Loot/*') do |k|
      name = k.elements['ItemName'].text
      itemid = Integer(k.elements['ItemID'].text.split(':')[0])
      awardee = k.elements['Player'].text
      loot << Hash[:itemname => name, :wowitemid => itemid, :awardee => awardee]
    end
    Hash[:boss => boss, :date => date, :attendees => chars, :loot => loot]
  end
  
  def self.class_map(xmlclass)
    classmap = Hash["DRUID" => "Druid",
           "MAGE" => "Mage",
           "ROGUE" => "Rogue",
           "DEATHKNIGHT" => "Death Knight",
           "WARRIOR" => "Warrior",
           "PALADIN" => "Paladin",
           "SHAMAN" => "Shaman",
           "HUNTER" => "Hunter",
           "WARLOCK" => "Warlock",
           "PRIEST" => "Priest"]
    return classmap[xmlclass]
  end
  
  def self.race_map(xmlrace)
    racemap = Hash["Scourge" => "Undead", "Orc" => "Orc", "BloodElf" => "Blood Elf", "Tauren" => "Tauren", "Troll" => "Troll"]
    return racemap[xmlrace]
  end
end
