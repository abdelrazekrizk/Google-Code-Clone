class BossesController < ApplicationController
  before_filter :login_required, :except => %w[index show]
  def index
    @bosses = Boss.find(:all, :order => "name ASC")
  end
  
  def show
    @boss = Boss.find(params[:id])
  end
  
  def adminindex
    @bosses = Boss.find(:all, :order => "name ASC")
  end
  
  def edit
    @boss = Boss.find(params[:id])
  end
  
  def update    
    b = Boss.find(params[:boss][:id])
    if(b.update_attributes(params[:boss]))
      flash[:notice] = "Updated successfully."
      redirect_to :action => 'adminindex'
    else
      flash[:error] = "Did not update successfully."
      redirect_to :action => 'edit'
    end
  end
  
  def destroy  
    if(Boss.find(params[:id]).destroy)
      flash[:notice] = "Deleted successfully"
    else
      flash[:error] = "Deletion unsuccessful"
    end
    redirect_to :action => 'adminindex'
  end
  
  def new
  end
  
  def create
    b = Boss.new
    if(b.update_attributes(params[:boss]))
      flash[:notice] = "Created successfully."
      redirect_to :action => 'adminindex'
    else
      flash[:error] = "Did not create successfully."
      redirect_to :action => 'create'
    end
  end
end
