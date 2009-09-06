class ForumForum < ActiveRecord::Base
  belongs_to :ForumCategory
  has_many :forum_topics, :order => "updated_at DESC"
end
