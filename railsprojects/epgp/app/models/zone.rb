class Zone < ActiveRecord::Base
  validates_presence_of :name
  has_many :bosses
end
