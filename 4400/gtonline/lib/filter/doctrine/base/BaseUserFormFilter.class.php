<?php

/**
 * User filter form base class.
 *
 * @package    gtonline
 * @subpackage filter
 * @author     Your name here
 * @version    SVN: $Id: sfDoctrineFormFilterGeneratedTemplate.php 29570 2010-05-21 14:49:47Z Kris.Wallsmith $
 */
abstract class BaseUserFormFilter extends BaseFormFilterDoctrine
{
  public function setup()
  {
    $this->setWidgets(array(
      'accountId'   => new sfWidgetFormDoctrineChoice(array('model' => $this->getRelatedModelName('Account'), 'add_empty' => true)),
      'sex'         => new sfWidgetFormFilterInput(array('with_empty' => false)),
      'birthday'    => new sfWidgetFormFilterDate(array('from_date' => new sfWidgetFormDate(), 'to_date' => new sfWidgetFormDate(), 'with_empty' => false)),
      'currentcity' => new sfWidgetFormFilterInput(),
      'hometown'    => new sfWidgetFormFilterInput(),
    ));

    $this->setValidators(array(
      'accountId'   => new sfValidatorDoctrineChoice(array('required' => false, 'model' => $this->getRelatedModelName('Account'), 'column' => 'id')),
      'sex'         => new sfValidatorSchemaFilter('text', new sfValidatorInteger(array('required' => false))),
      'birthday'    => new sfValidatorDateRange(array('required' => false, 'from_date' => new sfValidatorDate(array('required' => false)), 'to_date' => new sfValidatorDateTime(array('required' => false)))),
      'currentcity' => new sfValidatorPass(array('required' => false)),
      'hometown'    => new sfValidatorPass(array('required' => false)),
    ));

    $this->widgetSchema->setNameFormat('user_filters[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    $this->setupInheritance();

    parent::setup();
  }

  public function getModelName()
  {
    return 'User';
  }

  public function getFields()
  {
    return array(
      'id'          => 'Number',
      'accountId'   => 'ForeignKey',
      'sex'         => 'Number',
      'birthday'    => 'Date',
      'currentcity' => 'Text',
      'hometown'    => 'Text',
    );
  }
}
