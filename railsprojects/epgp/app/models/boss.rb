class Boss < ActiveRecord::Base
  validates_presence_of :name, :zone_id
  
  belongs_to :zone
  has_many :raids
  
  def desc
    self.zone.short_name + " - " + self.name
  end
  
  def self.get_base_value_by_name(bossname)
    return 100
  end
end
