class ItemsController < ApplicationController
  before_filter :login_required, :except => %w[index show]
  def index
    @items = Item.find(:all)
  end
  
  def show
    @items = Item.find_all_by_wowitemid(params[:id])
  end
  
  def adminindex
    @items = Item.find(:all, :joins => "LEFT JOIN raids on raids.id = items.raid_id", :order => "raids.time DESC")
  end
  
  def edit
    @item = Item.find(params[:id])
  end
  
  def update
    i = Item.find(params[:item][:id])
    if(i.update_attributes(params[:item]))
      flash[:notice] = "Item updated successfully"
      redirect_to :action => "adminindex"
    else
      flash[:notice] = "Did not update successfully"
      redirect_to :action => "edit"
    end
  end
  
  def new
  end
  
  def create
    i = Item.new
    if(i.update_attributes(params[:item]))
      flash[:notice] = "Item created successfully"
      redirect_to :action => "adminindex"
    else
      flash[:notice] = "Did not create successfully"
      redirect_to :action => "new"
    end
  end
  
  def destroy
    if(Item.find(params[:id]).destroy)
      flash[:notice] = "Item destroyed successfully"
    else
      flash[:error] = "Item could not be destroyed"
    end
    redirect_to :action => :adminindex
  end
end
