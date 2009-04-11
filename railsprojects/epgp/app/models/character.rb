class Character < ActiveRecord::Base
  validates_presence_of :name, :character_class, :character_race
  
  has_many :attendance_records
  has_many :raids, :through => :attendance_records
  has_many :items
  
  belongs_to :character_class
  belongs_to :character_race
  
  before_save :presave
  before_destroy :prevent_destroy
  
  def initialize(params = nil)
    super
    self.ep = 0
    self.gp = 0
    self.pr = 0
    self.is_active ||= true
  end
  
  def presave
    self.pr = self.ep / (self.gp.zero? ? 1000 : self.gp)
  end
  
  def prevent_destroy
    throw :never_destroy_characters_set_to_inactive
  end
end
