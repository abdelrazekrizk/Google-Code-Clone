class Item < ActiveRecord::Base
  belongs_to :raid
  belongs_to :character
  
  before_create :precreate
  before_update :preupdate
  before_destroy :predestroy
  
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
end
