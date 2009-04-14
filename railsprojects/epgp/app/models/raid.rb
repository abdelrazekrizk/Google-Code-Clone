class Raid < ActiveRecord::Base
  validates_presence_of :note, :boss, :time, :base_value
  validates_numericality_of :base_value 
  
  belongs_to :boss
  
  has_many :attendance_records
  has_many :characters, :through => :attendance_records
  has_many :items
  
  before_update :preupdate
  before_destroy :predestroy
  
  def initialize(params = nil)
    super
    self.inflated_multiplier = get_inflation_multiplier
    self.inflated_value = self.base_value * self.inflated_multiplier
  end
  
  def get_inflation_multiplier
    count = Decay.count(:conditions => ["performed_on < ?", self.time])
    (1.0 / 0.9) ** count
  end
  
  def preupdate
    self.inflated_multiplier = get_inflation_multiplier
    self.inflated_value = self.base_value * self.inflated_multiplier
    
    difference = self.inflated_value - Raid.find(self.id).inflated_value
    
    self.characters.each do |char|
      char.ep += difference
      char.save
    end
    
    self.items.each do |i|
      i.touch
      i.save # 'touches' each item and updates decay settings
    end
    
  end
  
  def decayed_value
    multiplier = Decay.get_max_inflation_multiplier
    return self.inflated_value / multiplier # yadda yadda, feels more natural this way
  end
  
  def predestroy
    self.attendance_records.each do |ar|
      ar.destroy
    end
    
    self.items.each do |i|
      i.destroy
    end
  end
  
  def date
    self.time.strftime('%m/%d/%y')
  end
  
  def desc
    self.date + " - " + self.boss.name
  end
  
  def attendees_as_str
    str = ""
    self.characters.each do |c|
      str += c.name + "\n"
    end
    return str
  end
  
  def attendees_as_str=(str)
    chars = []
    str.split("\r\n").each do |s|
      chars += [Character.find_by_name(s)] or throw :invalid_character_name
    end
    self.attendance_records.each do |e|
      e.destroy
    end
    self.attendance_records = []
    chars.each do |a|
      record = AttendanceRecord.create :raid_id => self.id, :character_id => a.id
      self.attendance_records += [record]
    end
  end
  
end
