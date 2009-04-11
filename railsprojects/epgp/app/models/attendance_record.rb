class AttendanceRecord < ActiveRecord::Base
  belongs_to :character
  belongs_to :raid
  
  before_create :credit_ep
  after_destroy :remove_ep
  
  def credit_ep
    character.reload
    raid.reload
    character.ep += raid.inflated_value
    character.save
  end
  
  def remove_ep
    character.reload
    raid.reload
    character.ep -= raid.inflated_value
    character.save
  end
  
end
