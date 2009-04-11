class Boss < ActiveRecord::Base
  validates_presence_of :name, :zone_id
  
  belongs_to :zone
end
