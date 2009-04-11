class CharacterClass < ActiveRecord::Base
  validates_presence_of :name
  has_many :characters
end
