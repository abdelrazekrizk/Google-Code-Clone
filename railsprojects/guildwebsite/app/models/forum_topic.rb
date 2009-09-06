class ForumTopic < ActiveRecord::Base
  belongs_to :user
  belongs_to :ForumForum
  has_many :forum_posts, :order => "created_at ASC"
end
