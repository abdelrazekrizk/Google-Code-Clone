class NewsPostsController < ApplicationController
  before_filter :check_admin, :except => [:index, :show]
  def initialize
    super
    @page_size = 5
  end
  
  def index
    @news_posts = NewsPost.all :order => "created_at DESC", :limit => @page_size 
  end

  def show
    @news_post = NewsPost.find(params[:id])
  end

  def new
    @news_post = NewsPost.new
  end

  def edit
    @news_post = NewsPost.find(params[:id])
  end

  def create
    params[:user] = session[:user]
    @news_post = NewsPost.new(params[:news_post])

    if @news_post.save
      flash[:notice] = 'NewsPost was successfully created.'
    end
  end

  def update
    @news_post = NewsPost.find(params[:id])

    if @news_post.update_attributes(params[:news_post])
      flash[:notice] = 'NewsPost was successfully updated.'
    end
  end

  def destroy
    @news_post = NewsPost.find(params[:id])
    @news_post.destroy

    redirect_to(news_posts_url)
  end
  
  private 
  
  def check_admin
    unless logged_in? and current_user.has_permission :admin
      redirect_to :action => :index
    end
  end
end
