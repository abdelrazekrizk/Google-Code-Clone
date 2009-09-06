class ForumForumsController < ApplicationController
  # GET /forum_forums
  # GET /forum_forums.xml
  def index
    @forum_forums = ForumForum.all

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @forum_forums }
    end
  end

  # GET /forum_forums/1
  # GET /forum_forums/1.xml
  def show
    @forum = ForumForum.find(params[:id])
    @stickied = @forum.forum_topics.find(:all, :conditions => "stickied = true") # todo: add paging limiters
    @topics = @forum.forum_topics.find(:all, :conditions => "stickied = false")

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @forum }
    end
  end

  # GET /forum_forums/new
  # GET /forum_forums/new.xml
  def new
    @forum_forum = ForumForum.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @forum_forum }
    end
  end

  # GET /forum_forums/1/edit
  def edit
    @forum_forum = ForumForum.find(params[:id])
  end

  # POST /forum_forums
  # POST /forum_forums.xml
  def create
    @forum_forum = ForumForum.new(params[:forum_forum])

    respond_to do |format|
      if @forum_forum.save
        flash[:notice] = 'ForumForum was successfully created.'
        format.html { redirect_to(@forum_forum) }
        format.xml  { render :xml => @forum_forum, :status => :created, :location => @forum_forum }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @forum_forum.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /forum_forums/1
  # PUT /forum_forums/1.xml
  def update
    @forum_forum = ForumForum.find(params[:id])

    respond_to do |format|
      if @forum_forum.update_attributes(params[:forum_forum])
        flash[:notice] = 'ForumForum was successfully updated.'
        format.html { redirect_to(@forum_forum) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @forum_forum.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /forum_forums/1
  # DELETE /forum_forums/1.xml
  def destroy
    @forum_forum = ForumForum.find(params[:id])
    @forum_forum.destroy

    respond_to do |format|
      format.html { redirect_to(forum_forums_url) }
      format.xml  { head :ok }
    end
  end
end
