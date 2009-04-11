class Decay < ActiveRecord::Base
  after_create :touch_raids
  after_destroy :touch_raids

  def touch_raids
    raids_after.each do |r|
      r.save # 'touches' the data, should decay
    end
  end
  
  def raids_after
    Raid.find :all, :conditions => ["time > ?", performed_on]
  end
  
  def self.get_max_inflation_multiplier
    (1.0 / 0.9) ** Decay.count
  end
  
end
