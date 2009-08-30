class ForumCategory < ActiveRecord::Base
  has_many :forum_forums
end
