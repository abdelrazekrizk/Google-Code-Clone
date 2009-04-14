class Item < ActiveRecord::Base
  belongs_to :raid
  belongs_to :character
  
  before_create :precreate
  before_update :preupdate
  before_destroy :predestroy
  
  def touch
    self.inflated_value = self.inflated_value
  end
  
  def precreate
    self.inflated_value = raid.inflated_multiplier * self.base_value
    character.gp += self.inflated_value
    character.save
  end
  
  def preupdate
    self.inflated_value = raid.inflated_multiplier * self.base_value
    difference = self.inflated_value - Item.find(self.id).inflated_value
    character.gp += difference
    character.save
  end
  
  def predestroy
    character.gp -= Item.find(self.id).inflated_value
    character.save
  end
  
  def decayed_value
    return self.inflated_value / Decay.get_max_inflation_multiplier
  end
  
  def self.get_value_for(wowitemid)
    item = self.find_by_wowitemid(wowitemid)
    if(item.nil?)
      return 0
    else
      return item.base_value
    end
  end
end
